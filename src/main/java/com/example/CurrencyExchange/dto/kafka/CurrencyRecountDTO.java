package com.example.CurrencyExchange.dto.kafka;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRecountDTO {

    private HashMap<String, BigDecimal> currencyRates;
    private List<StoredCurrencyDTO> storedCurrencies;
}
