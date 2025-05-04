package com.example.CurrencyExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredCurrencyDTO {

    private UUID id;
    private BigDecimal count;
    private UUID currencyId;
    private UUID cashRegister;
}
