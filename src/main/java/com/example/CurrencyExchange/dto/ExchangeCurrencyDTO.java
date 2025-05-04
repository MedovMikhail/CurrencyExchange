package com.example.CurrencyExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyDTO {

    private Long id;
    private BigDecimal countBaseCash;
    private BigDecimal countTargetCash;
    private BigDecimal exchangeRate;
    private ZonedDateTime dateOfExchange;
    private Long baseCurrencyId;
    private Long targetCurrencyId;
    private Long cashRegisterId;
    private Long userId;
}
