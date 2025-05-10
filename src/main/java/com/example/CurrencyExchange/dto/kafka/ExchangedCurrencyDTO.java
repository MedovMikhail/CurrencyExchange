package com.example.CurrencyExchange.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangedCurrencyDTO {

    private BigDecimal baseStoredCurrencyDiff;
    private BigDecimal targetStoredCurrencyDiff;
    private ZonedDateTime dateOfExchange;
}
