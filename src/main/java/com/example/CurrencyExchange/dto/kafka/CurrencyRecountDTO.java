package com.example.CurrencyExchange.dto.kafka;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRecountDTO {

    private StoredCurrencyDTO base;
    private List<StoredCurrencyDTO> others;
}
