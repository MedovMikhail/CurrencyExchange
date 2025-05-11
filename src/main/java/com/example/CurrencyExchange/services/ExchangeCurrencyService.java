package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.dto.kafka.ExchangeValuesDTO;
import com.example.CurrencyExchange.dto.kafka.ExchangedCurrencyDTO;
import com.example.CurrencyExchange.entities.ExchangeCurrency;
import com.example.CurrencyExchange.kafka.KafkaService;
import com.example.CurrencyExchange.repositories.ExchangeCurrencyRepository;
import com.example.CurrencyExchange.utils.mapping.ExchangeCurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeCurrencyService {

    @Autowired
    private ExchangeCurrencyMapper exchangeCurrencyMapper;
    @Autowired
    private ExchangeCurrencyRepository exchangeCurrencyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CashRegisterService cashRegisterService;
    @Autowired
    private StoredCurrencyService storedCurrencyService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private KafkaService kafkaService;

    public List<ExchangeCurrencyDTO> getExchangeCurrencies(){
        return exchangeCurrencyRepository.findAll(Sort.by("id"))
                .stream()
                .map(exchangeCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public List<ExchangeCurrencyDTO> getExchangeCurrencies(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return exchangeCurrencyRepository.findAll(pageable)
                .stream()
                .map(exchangeCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public List<ExchangeCurrencyDTO> getExchangeCurrenciesByBaseAndTargetCurrencyCode(String baseCode, String targetCode) {
        // если какого-то параметра нет, то присваиваем ему пустую строку
        if (baseCode == null) baseCode = "";
        if (targetCode == null) targetCode = "";
        // убираем пробелы
        baseCode = baseCode.strip();
        targetCode = targetCode.strip();
        return exchangeCurrencyRepository.findByBaseCurrencyCodeContainingIgnoreCaseAndTargetCurrencyCodeContainingIgnoreCase(
                baseCode, targetCode)
                .stream()
                .map(exchangeCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public List<ExchangeCurrencyDTO> getExchangeCurrenciesByDate(Date startDate, Date endDate) {
        // конвертируем date в zonedDateTime для московского региона
        ZonedDateTime startZonedDateTime = startDate.toInstant().atZone(ZoneId.of("Europe/Moscow"));
        ZonedDateTime endZonedDateTime = endDate.toInstant().atZone(ZoneId.of("Europe/Moscow"));
        return exchangeCurrencyRepository.findAllByDateOfExchangeBetween(
                startZonedDateTime, endZonedDateTime)
                .stream()
                .map(exchangeCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public ExchangeCurrencyDTO getExchangeCurrency(Long id) {
        return exchangeCurrencyMapper.fromEntityToDTO(
                exchangeCurrencyRepository.findById(id).orElse(null)
        );
    }

    // добавление обмена валют
    public ExchangeCurrencyDTO addExchangeCurrency(
            Long userId, Long cashRegId,
            ExchangeCurrencyDTO exchangeCurrencyDTO
    ) {
        // проверяем существование пользователя и кассы
        if (userService.getUser(userId) == null || cashRegisterService.getCashRegister(cashRegId) == null) return null;

        // получаем валюту из которой переводят и в которую переводят
        CurrencyDTO baseCurrencyDTO = currencyService.getCurrency(
                exchangeCurrencyDTO.getBaseCurrencyCode()
        );
        CurrencyDTO targetCurrencyDTO = currencyService.getCurrency(
                exchangeCurrencyDTO.getTargetCurrencyCode()
        );

        // проверяем существование валют
        if (baseCurrencyDTO == null || targetCurrencyDTO == null) return null;

        // создаем ключ для сообщений в кафке
        String key = new Date().toString();

        // отправляем в кафку сообщение с курсами валют и ожидаем получения их соотношения
        BigDecimal exchangeRate = kafkaService.sendAndWaitCurrencyScale(
                baseCurrencyDTO.getCode(),
                targetCurrencyDTO.getCode(),
                key
        );
        // сообщение обратно не пришло
        if (exchangeRate == null) return null;

        // получаем количество денег в кассе в обоих валютах
        StoredCurrencyDTO baseStoredCurrency = storedCurrencyService.getStoredCurrencyByCodeFromCashRegister(
                cashRegId, baseCurrencyDTO.getCode()
        );
        StoredCurrencyDTO targetStoredCurrency = storedCurrencyService.getStoredCurrencyByCodeFromCashRegister(
                cashRegId, targetCurrencyDTO.getCode()
        );

        BigDecimal baseCashRegisterCount = baseStoredCurrency.getCount();
        BigDecimal targetCashRegisterCount = targetStoredCurrency.getCount();

        // проверяем хранит ли касса деньги в этих валютах
        if (baseCashRegisterCount == null || targetCashRegisterCount == null) return null;

        // создаем объект со значениями обмена валют
        ExchangeValuesDTO exchangeValuesDTO = new ExchangeValuesDTO(
                exchangeCurrencyDTO.getCountBaseCash(),
                exchangeRate,
                baseCashRegisterCount,
                targetCashRegisterCount
        );

        // ожидаем прихода обработанного обмена валют
        ExchangedCurrencyDTO exchangedCurrencyDTO = kafkaService.sendAndWaitExchangedCurrency(exchangeValuesDTO, key);
        if (exchangedCurrencyDTO == null) return null;

        // заполняем поля обмена валют
        exchangeCurrencyDTO = exchangeCurrencyMapper.fromExchangedToExchange(exchangeCurrencyDTO, exchangedCurrencyDTO);
        exchangeCurrencyDTO.setUserId(userId);
        exchangeCurrencyDTO.setCashRegisterId(cashRegId);

        ExchangeCurrency exchangeCurrency = exchangeCurrencyMapper.fromDTOToEntity(
                exchangeCurrencyDTO, baseCurrencyDTO.getId(), targetCurrencyDTO.getId()
        );

        try {
            // пытаемся сохранить обмен в базе
            exchangeCurrency = exchangeCurrencyRepository.save(exchangeCurrency);
            // получаем dto от сущности
            exchangeCurrencyDTO = exchangeCurrencyMapper.fromEntityToDTO(
                    exchangeCurrency,
                    baseCurrencyDTO.getCode(),
                    targetCurrencyDTO.getCode()
            );

            // вносим изменения в хранимые валюты кассы
            storedCurrencyService.changeCountStoredCurrency(
                    baseStoredCurrency.getId(),
                    exchangedCurrencyDTO.getBaseStoredCurrencyDiff(),
                    true
            );
            storedCurrencyService.changeCountStoredCurrency(
                    targetStoredCurrency.getId(),
                    exchangedCurrencyDTO.getTargetStoredCurrencyDiff(),
                    false
            );
            return exchangeCurrencyDTO;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void deleteExchangeCurrency(Long id) {
        exchangeCurrencyRepository.deleteById(id);
    }
    
}
