package com.example.CurrencyExchange.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {
    @Value("${kafka.topic.sendToCurrenciesRate}")
    private String sendToCurrenciesRate;
    @Value("${kafka.topic.waitCurrenciesRate}")
    private String waitCurrenciesRate;
    @Value("${kafka.topic.sendToExchangeCurrencies}")
    private String sendToExchangeCurrencies;
    @Value("${kafka.topic.waitExchangeCurrencies}")
    private String waitExchangeCurrencies;

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(sendToCurrenciesRate).build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name(waitCurrenciesRate).build();
    }

    @Bean
    public NewTopic topic3() {
        return TopicBuilder.name(sendToExchangeCurrencies).build();
    }

    @Bean
    public NewTopic topic4() {
        return TopicBuilder.name(waitExchangeCurrencies).build();
    }
}
