package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.dto.kafka.CurrencyCodesMessage;
import com.example.CurrencyExchange.entities.ExchangeCurrency;
import com.example.CurrencyExchange.kafka.consumer.KafkaConsumers;
import com.example.CurrencyExchange.kafka.producer.KafkaSender;
import com.example.CurrencyExchange.repositories.ExchangeCurrencyRepository;
import com.example.CurrencyExchange.utils.mapping.ExchangeCurrencyMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
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
    private CurrencyService currencyService;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private KafkaConsumers kafkaConsumers;

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
            ExchangeCurrencyDTO exchangeCurrencyRequestDTO
    ) {
        /*
            if user and cashRegister exists
         */
        if (userService.getUser(userId) == null || cashRegisterService.getCashRegister(cashRegId) == null) return null;

        CurrencyDTO baseCurrencyDTO = currencyService.getCurrency(
                exchangeCurrencyRequestDTO.getBaseCurrencyCode()
        );
        CurrencyDTO targetCurrencyDTO = currencyService.getCurrency(
                exchangeCurrencyRequestDTO.getTargetCurrencyCode()
        );
        /*
            if baseCurrency and targetCurrency exists
         */
        if (baseCurrencyDTO == null || targetCurrencyDTO == null) return null;

        exchangeCurrencyRequestDTO.setUserId(userId);
        exchangeCurrencyRequestDTO.setCashRegisterId(cashRegId);

        ExchangeCurrency exchangeCurrency = exchangeCurrencyMapper.fromDTOToEntity(
                exchangeCurrencyRequestDTO, baseCurrencyDTO.getId(), targetCurrencyDTO.getId()
        );

        String key = new Date().toString();
        BigDecimal exchangeRate = null;
        CurrencyCodesMessage codesMessage = new CurrencyCodesMessage(
                baseCurrencyDTO.getCode(), targetCurrencyDTO.getCode()
        );
        kafkaSender.sendMessage(codesMessage,"request-currency-rate", key);
        try (KafkaConsumer<String, String> consumer = kafkaConsumers.currencyRateConsumer()) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(5000));
            for (ConsumerRecord<String, String> record: consumerRecords) {
                if (record.key().equals(key)){
                    exchangeRate = new BigDecimal(record.value());
                    break;
                }
            }
        }

        if (exchangeRate == null) return null;

        exchangeCurrency.setExchangeRate(exchangeRate);
        exchangeCurrency.setCountTargetCash(
                exchangeCurrency.getCountBaseCash().multiply(exchangeCurrency.getExchangeRate())
        );

        exchangeCurrency.setDateOfExchange(ZonedDateTime.now(ZoneId.of("Europe/Moscow")));

        try {
            exchangeCurrency = exchangeCurrencyRepository.save(exchangeCurrency);
        } catch (RuntimeException e) {
            return null;
        }
        ExchangeCurrencyDTO exchangeCurrencyDTO = exchangeCurrencyMapper.fromEntityToDTO(exchangeCurrency);
        exchangeCurrencyDTO.setBaseCurrencyCode(baseCurrencyDTO.getCode());
        exchangeCurrencyDTO.setTargetCurrencyCode(targetCurrencyDTO.getCode());
        return exchangeCurrencyDTO;
    }

    public ExchangeCurrencyDTO updateExchangeCurrency(Long id, BigDecimal countBaseCash) {
        ExchangeCurrency exchangeCurrency = exchangeCurrencyRepository.findById(id).orElse(null);

        if (exchangeCurrency == null) return null;

        exchangeCurrency.setCountBaseCash(countBaseCash);

        try {
            exchangeCurrency = exchangeCurrencyRepository.save(exchangeCurrency);
        } catch (RuntimeException e) {
            return null;
        }

        return exchangeCurrencyMapper.fromEntityToDTO(exchangeCurrency);
    }

    public void deleteExchangeCurrency(Long id) {
        exchangeCurrencyRepository.deleteById(id);
    }
    
}
