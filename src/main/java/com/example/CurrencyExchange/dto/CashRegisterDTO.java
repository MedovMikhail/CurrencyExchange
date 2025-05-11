package com.example.CurrencyExchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashRegisterDTO {

    @Schema(description = "id кассы")
    private Long id;
    @Schema(description = "адрес расположения кассы", example = "г.Самара ул.Московское шоссе д.3")
    private String address;
}
