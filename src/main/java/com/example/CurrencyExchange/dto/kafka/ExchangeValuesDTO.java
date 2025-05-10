package com.example.CurrencyExchange.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeValuesDTO {

    private BigDecimal baseCurrencyCount;
    private BigDecimal exchangeRate;
    private BigDecimal baseStoredCurrency;
    private BigDecimal targetStoredCurrency;
}
