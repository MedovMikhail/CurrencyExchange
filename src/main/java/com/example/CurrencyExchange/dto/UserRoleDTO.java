package com.example.CurrencyExchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {

    @Schema(description = "id роли")
    private Long id;
    @Schema(description = "название роли", example = "ADMIN")
    private String name;
}
