package com.example.CurrencyExchange.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyCodesMessage {

    private String baseCurrencyCode;
    private String targetCurrencyCode;
}
