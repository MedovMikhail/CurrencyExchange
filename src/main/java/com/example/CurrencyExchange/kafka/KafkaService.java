package com.example.CurrencyExchange.kafka;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.dto.kafka.CurrencyCodesMessage;
import com.example.CurrencyExchange.dto.kafka.CurrencyRecountDTO;
import com.example.CurrencyExchange.dto.kafka.ExchangeValuesDTO;
import com.example.CurrencyExchange.dto.kafka.ExchangedCurrencyDTO;
import com.example.CurrencyExchange.kafka.consumer.KafkaConsumerListener;
import com.example.CurrencyExchange.kafka.producer.KafkaProducerSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class KafkaService {

    @Value("${kafka.topic.sendToCurrenciesRate}")
    private String sendToCurrenciesRate;
    @Value("${kafka.topic.waitCurrenciesRate}")
    private String waitCurrenciesRate;
    @Value("${kafka.topic.sendToCurrenciesScale}")
    private String sendToCurrenciesScale;
    @Value("${kafka.topic.waitCurrenciesScale}")
    private String waitCurrenciesScale;
    @Value("${kafka.topic.sendToExchangeCurrencies}")
    private String sendToExchangeCurrencies;
    @Value("${kafka.topic.waitExchangeCurrencies}")
    private String waitExchangeCurrencies;
    @Value("${kafka.topic.sendToCurrencyRates}")
    private String sendToCurrencyRates;
    @Value("${kafka.topic.waitCurrencyRates}")
    private String waitCurrencyRates;
    @Value("${kafka.topic.sendToRecountCurrency}")
    private String sendToRecountCurrency;
    @Value("${kafka.topic.waitRecountCurrency}")
    private String waitRecountCurrency;

    @Autowired
    private KafkaProducerSender kafkaSender;
    @Autowired
    private KafkaConsumerListener kafkaConsumerListener;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(Object message, String topic, @Nullable String key) {
        if (key == null) kafkaSender.sendMessage(message, topic);
        else kafkaSender.sendMessage(message, topic, key);
    }

    public String waitMessage(String topic, String key) {
        try (KafkaConsumer<String, String> consumer = kafkaConsumerListener.getConsumer(topic)) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(5000));
            for (ConsumerRecord<String, String> record: consumerRecords) {
                if (record.key().equals(key)){
                    return record.value();
                }
            }
        }
        return null;
    }

    public BigDecimal sendAndWaitCurrencyScale(String baseCurrencyCode, String targetCurrencyCode, String key) {
        BigDecimal exchangeRate;
        // создаем объект с кодами валют
        CurrencyCodesMessage codesMessage = new CurrencyCodesMessage(
                baseCurrencyCode, targetCurrencyCode
        );
        // отправляем сообщение для с кодами валют, чтобы получить их соотношение
        sendMessage(codesMessage, sendToCurrenciesScale, key);
        // ожидаем получения сообщения с соотношением валют
        String answer = waitMessage(waitCurrenciesScale, key);

        if (answer == null) return null;

        exchangeRate = new BigDecimal(answer);
        return exchangeRate;
    }

    public BigDecimal sendAndWaitCurrencyRate(String currencyCode, String key) {
        BigDecimal exchangeRate;
        // отправляем сообщение для с кодами валют, чтобы получить их соотношение
        sendMessage(currencyCode, sendToCurrenciesRate, key);
        // ожидаем получения сообщения с соотношением валют
        String answer = waitMessage(waitCurrenciesRate, key);

        if (answer == null) return null;

        exchangeRate = new BigDecimal(answer);
        return exchangeRate;
    }

    public HashMap<String, BigDecimal> sendAndWaitCurrencyRates(List<String> currencyCodes, String key) {
        sendMessage(currencyCodes, sendToCurrencyRates, key);
        String answer = waitMessage(waitCurrencyRates, key);
        if (answer == null) return null;
        try {
            return objectMapper.readValue(answer, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StoredCurrencyDTO> sendAndWaitRecountCurrency(CurrencyRecountDTO recountDTO, String key) {
        sendMessage(recountDTO, sendToRecountCurrency, key);
        String answer = waitMessage(waitRecountCurrency, key);

        if (answer == null) return null;

        try {
            return objectMapper.readValue(answer, ArrayList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangedCurrencyDTO sendAndWaitExchangedCurrency(ExchangeValuesDTO exchangeValuesDTO, String key) {
        // отправляем объект со значениями обмена в кафку
        sendMessage(exchangeValuesDTO,sendToExchangeCurrencies, key);
        // ожидаем сообщения из кафки с обработанными значениями сделки
        String answer = waitMessage(waitExchangeCurrencies, key);

        if (answer == null || answer.equals("Not enough cash")) return null;

        ExchangedCurrencyDTO exchangedCurrencyDTO;
        try {
            exchangedCurrencyDTO = objectMapper.readValue(answer, ExchangedCurrencyDTO.class);
            return exchangedCurrencyDTO;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
