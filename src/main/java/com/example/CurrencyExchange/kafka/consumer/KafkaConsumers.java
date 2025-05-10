package com.example.CurrencyExchange.kafka.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumers {

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    public KafkaConsumer<String, String> currencyRateConsumer() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaConsumerConfig.config());
        consumer.subscribe(List.of("get-currency-rate"));
        return consumer;
    }
}
