package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.SafetyUserDTO;
import com.example.CurrencyExchange.dto.UserDTO;
import com.example.CurrencyExchange.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить список всех пользователей",
            description = "В ответе возвращается список User.")
    @GetMapping
    public ResponseEntity<List<SafetyUserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить страницу пользователей",
            description = "В ответе возвращается список User.")
    @GetMapping("/page")
    public ResponseEntity<List<SafetyUserDTO>> getPagingUsers(@RequestParam @Validated int pageNumber, @RequestParam @Validated int pageSize) {
        return ResponseEntity.ok(userService.getUsers(pageNumber, pageSize));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить пользователя по id",
            description = "В ответе возвращается User.")
    @GetMapping("/{id}")
    public ResponseEntity<SafetyUserDTO> getUserById(@PathVariable Long id) {
        SafetyUserDTO safetyUserDTO = userService.getUser(id);
        return safetyUserDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(safetyUserDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить информацию о пользователе",
            description = "В ответе возвращается User.")
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<SafetyUserDTO> putUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        SafetyUserDTO safetyUserDTO = userService.updateUser(id, userDTO);
        return safetyUserDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(safetyUserDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить пользователя",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
