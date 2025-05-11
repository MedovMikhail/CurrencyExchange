package com.example.CurrencyExchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyDTO {

    @Schema(description = "id обмена валют")
    private Long id;
    @Schema(description = "количество денег у пользователя для обмена", example = "1000")
    private BigDecimal countBaseCash;
    @Schema(description = "количество денег, которые получит пользователь при обмене (в целевой валюте)", example = "200")
    private BigDecimal countTargetCash;
    @Schema(description = "соотношение курсов валют", example = "0.2")
    private BigDecimal exchangeRate;
    @Schema(description = "дата и время совершения обмена")
    private ZonedDateTime dateOfExchange;
    @Schema(description = "валюта, которая есть у пользователя", example = "RUB")
    private String baseCurrencyCode;
    @Schema(description = "валюта, в которую пользователь хочет обменять", example = "USD")
    private String targetCurrencyCode;
    @Schema(description = "id кассы")
    private Long cashRegisterId;
    @Schema(description = "id пользователя")
    private Long userId;
}
