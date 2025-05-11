package com.example.CurrencyExchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(description = "id пользователя")
    private Long id;
    @Schema(description = "ФИО", example = "Медов Михаил Андреевич")
    private String name;
    @Schema(description = "номер телефона", example = "9275553498")
    private String phone;
    @Schema(description = "почта", example = "youraddres@gmail.com")
    private String email;
    @Schema(description = "пароль")
    private String password;
    @Schema(description = "id роли пользователя")
    private Long roleId;
}
