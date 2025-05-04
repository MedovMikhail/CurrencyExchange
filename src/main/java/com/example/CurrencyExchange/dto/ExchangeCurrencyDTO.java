package com.example.CurrencyExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyDTO {

    private UUID id;
    private BigDecimal countBaseCash;
    private BigDecimal countTargetCash;
    private BigDecimal exchangeRate;
    private ZonedDateTime dateOfExchange;
    private UUID baseCurrencyId;
    private UUID targetCurrencyId;
    private UUID cashRegisterId;
    private UUID userId;
}
