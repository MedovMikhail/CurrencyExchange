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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        return exchangeCurrencyRepository.findAll()
                .stream()
                .map(exchangeCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public ExchangeCurrencyDTO getExchangeCurrency(Long id) {
        return exchangeCurrencyMapper.fromEntityToDTO(
                exchangeCurrencyRepository.findById(id).orElse(null)
        );
    }

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

        BigDecimal exchangeRate = kafkaService.sendAndWaitCurrencyScale(
                baseCurrencyDTO.getCode(),
                targetCurrencyDTO.getCode(),
                key
        );
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

        exchangeCurrencyDTO = exchangeCurrencyMapper.fromExchangedToExchange(exchangeCurrencyDTO, exchangedCurrencyDTO);
        exchangeCurrencyDTO.setUserId(userId);
        exchangeCurrencyDTO.setCashRegisterId(cashRegId);

        ExchangeCurrency exchangeCurrency = exchangeCurrencyMapper.fromDTOToEntity(
                exchangeCurrencyDTO, baseCurrencyDTO.getId(), targetCurrencyDTO.getId()
        );

        try {
            exchangeCurrency = exchangeCurrencyRepository.save(exchangeCurrency);
            exchangeCurrencyDTO = exchangeCurrencyMapper.fromEntityToDTO(
                    exchangeCurrency,
                    baseCurrencyDTO.getCode(),
                    targetCurrencyDTO.getCode()
            );

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
