package com.example.CurrencyExchange.kafka.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumerListener {

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    // получить (создать) консьюмера, который будет слушать определенный топик
    public KafkaConsumer<String, String> getConsumer(String topic) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaConsumerConfig.config());
        consumer.subscribe(List.of(topic));
        return consumer;
    }
}
