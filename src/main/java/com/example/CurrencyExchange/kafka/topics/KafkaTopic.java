package com.example.CurrencyExchange.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


// создание топиков
@Configuration
public class KafkaTopic {
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
    public NewTopic topic5() {
        return TopicBuilder.name(sendToCurrenciesScale).build();
    }

    @Bean
    public NewTopic topic6() {
        return TopicBuilder.name(waitCurrenciesScale).build();
    }

    @Bean
    public NewTopic topic7() {
        return TopicBuilder.name(sendToCurrencyRates).build();
    }

    @Bean
    public NewTopic topic8() {
        return TopicBuilder.name(waitCurrencyRates).build();
    }

    @Bean
    public NewTopic topic9() {
        return TopicBuilder.name(sendToRecountCurrency).build();
    }

    @Bean
    public NewTopic topic10() {
        return TopicBuilder.name(waitRecountCurrency).build();
    }
}
