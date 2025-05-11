package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.dto.kafka.CurrencyRecountDTO;
import com.example.CurrencyExchange.entities.StoredCurrency;
import com.example.CurrencyExchange.kafka.KafkaService;
import com.example.CurrencyExchange.repositories.StoredCurrencyRepository;
import com.example.CurrencyExchange.utils.mapping.StoredCurrencyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class StoredCurrencyService {

    @Autowired
    private StoredCurrencyRepository storedCurrencyRepository;
    @Autowired
    private StoredCurrencyMapper storedCurrencyMapper;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CashRegisterService registerService;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private ObjectMapper objectMapper;

    public List<StoredCurrencyDTO> getStoredCurrencies() {
        return storedCurrencyRepository.findAll(Sort.by("id"))
                .stream()
                .map(storedCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public List<StoredCurrencyDTO> getStoredCurrencies(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return storedCurrencyRepository.findAll(pageable)
                .stream()
                .map(storedCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public List<StoredCurrencyDTO> getStoredCurrenciesByCashRegister(Long cashRegisterId) {
        if (registerService.getCashRegister(cashRegisterId) == null) return null;
        return storedCurrencyRepository.findByCashRegisterId(cashRegisterId)
                .stream()
                .map(storedCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public StoredCurrencyDTO getStoredCurrency(Long id) {
        return storedCurrencyMapper.fromEntityToDTO(
                storedCurrencyRepository.findById(id).orElse(null)
        );
    }

    public StoredCurrencyDTO getStoredCurrencyByCodeFromCashRegister(Long cashRegId, String code) {
        return storedCurrencyMapper.fromEntityToDTO(
                storedCurrencyRepository.getStoredCurrencyByCode(cashRegId, code).orElse(null)
        );
    }

    // добавляем новую хранимую валюту в кассу
    public StoredCurrencyDTO addStoredCurrency(Long curId, Long cashRegId, StoredCurrencyDTO storedCurrencyDTO) {
        CurrencyDTO currencyDTO = currencyService.getCurrency(curId);
        // проверяем существует ли валюта и касса по их id
        if (currencyDTO == null || registerService.getCashRegister(cashRegId) == null) return null;

        // отправляем в kafka сообщение с кодом валюты и ожидаем получения её курса
        BigDecimal exchangeRate = kafkaService.sendAndWaitCurrencyRate(currencyDTO.getCode(), new Date().toString());
        // что-то пошло не так
        if (exchangeRate == null) return null;
        // заполняем поля хранимой валюты
        storedCurrencyDTO.setCashRegisterId(cashRegId);
        storedCurrencyDTO.setExchangeRate(exchangeRate);
        StoredCurrency storedCurrency = storedCurrencyMapper.fromDTOToEntity(storedCurrencyDTO, curId);
        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
        }
        catch (Exception e){
            return null;
        }
        return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
    }

    public StoredCurrencyDTO updateStoredCurrency(Long id, StoredCurrencyDTO storedCurrencyDTO) {
        // проверяем существует ли хранимая валюта по id
        StoredCurrency storedCurrency = storedCurrencyRepository.findById(id).orElse(null);
        if (storedCurrency == null) return null;

        // устанавливаем новое количество валюты в кассе
        storedCurrency.setCount(storedCurrencyDTO.getCount());
        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
            return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    // изменяем количество валюты в кассе на count, если isAdd true, то прибавляем, если false, то убавляем
    public StoredCurrencyDTO changeCountStoredCurrency(Long id, BigDecimal count, boolean isAdd) {
        // проверяем существует ли хранимая валюта по id
        StoredCurrency storedCurrency = storedCurrencyRepository.findById(id).orElse(null);
        if (storedCurrency == null) return null;

        if (isAdd) {
            // добавляем к текущему количеству валюты
            storedCurrency.setCount(
                    storedCurrency.getCount().add(count)
            );
        } else {
            // убавляем от текущего количества валюты
            storedCurrency.setCount(
                    storedCurrency.getCount().subtract(count)
            );
        }

        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
            return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    // пересчет валюты в кассе в соответствии с актуальным курсом
    public List<StoredCurrencyDTO> recountCurrencies(Long cashRegId) {
        // получаем список хранимой валюты в кассе
        List<StoredCurrencyDTO> storedCurrenciesDTO = getStoredCurrenciesByCashRegister(cashRegId);
        if (storedCurrenciesDTO == null) return null;
        // получаем список кодов валют, которые хранятся в кассе
        List<String> currencies = storedCurrenciesDTO
                .stream()
                .map(StoredCurrencyDTO::getCurrencyCode)
                .toList();
        String key = new Date().toString();
        // отправляем сообщение в kafka и ожидаем получения маппы с кодами и их актуальными курсами
        HashMap<String, BigDecimal> currencyRates = kafkaService.sendAndWaitCurrencyRates(currencies, key);
        if (currencyRates == null || currencyRates.size() < currencies.size()) return null;

        CurrencyRecountDTO currencyRecountDTO = new CurrencyRecountDTO(currencyRates, storedCurrenciesDTO);
        // отправляем сообщение в kafka и ожидаем получения пересчитанных хранимых валют
        storedCurrenciesDTO = kafkaService.sendAndWaitRecountCurrency(currencyRecountDTO, key);

        // проверяем все ли валюты вернулись к нам
        if (storedCurrenciesDTO == null || storedCurrenciesDTO.size() < currencies.size()) return null;

        // преобразовываем dto в сущности
        List<StoredCurrency> storedCurrencyIterable = new ArrayList<>();
        for (StoredCurrencyDTO dto: storedCurrenciesDTO) {
            storedCurrencyIterable.add(
                    storedCurrencyMapper.fromDTOToEntity(dto)
            );
        }

        // сохраняем все пересчитанные хранимые валюты в базу
        List<StoredCurrency> storedCurrencies = storedCurrencyRepository.saveAll(
                storedCurrencyIterable
        );

        return storedCurrencies
                .stream()
                .map(storedCurrencyMapper::fromEntityToDTO)
                .toList();
    }
    
    public void deleteStoredCurrency(Long id) {
        storedCurrencyRepository.deleteById(id);
    }
}
