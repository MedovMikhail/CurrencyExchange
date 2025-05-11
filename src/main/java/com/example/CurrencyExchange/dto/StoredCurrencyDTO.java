package com.example.CurrencyExchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredCurrencyDTO {

    @Schema(description = "id пользователя")
    private Long id;
    @Schema(description = "количество валюты", example = "2500")
    private BigDecimal count;
    @Schema(description = "код валюты", example = "RUB")
    private String currencyCode;
    @Schema(description = "курс валюты (в соотношении с долларом)", example = "1.2")
    private BigDecimal exchangeRate;
    @Schema(description = "id кассы")
    private Long cashRegisterId;
    @Schema(description = "id валюты")
    private Long currencyId;
}
