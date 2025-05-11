package com.example.CurrencyExchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

    @Schema(description = "id валюты")
    private Long id;
    @Schema(description = "Название валюты", example = "Рубль")
    private String name;
    @Schema(description = "Код валюты", example = "RUB")
    private String code;
}
