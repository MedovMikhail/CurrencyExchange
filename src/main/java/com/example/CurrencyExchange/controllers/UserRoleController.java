package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.services.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Пользовательские роли")
@RestController
@RequestMapping("/roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить список всех пользовательских ролей",
            description = "В ответе возвращается список UserRole.")
    @GetMapping
    public List<UserRoleDTO> getUserRoles() {
        return userRoleService.getUserRoles();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить страницу пользовательских ролей",
            description = "В ответе возвращается список UserRole.")
    @GetMapping("/page")
    public List<UserRoleDTO> getPagingUserRoles(@RequestParam @Validated int pageNumber, @RequestParam @Validated int pageSize) {
        return userRoleService.getUserRoles(pageNumber, pageSize);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить пользовательскую роль по id",
            description = "В ответе возвращается UserRole.")
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getUserRole(@PathVariable Long id) {
        UserRoleDTO userRoleDTO = userRoleService.getUserRole(id);
        return userRoleDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(userRoleDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавить пользовательскую роль",
            description = "В ответе возвращается UserRole.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PostMapping
    public ResponseEntity<UserRoleDTO> postUserRole(@RequestParam String name) {
        UserRoleDTO userRoleDTO = userRoleService.addUserRole(name);
        return userRoleDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(userRoleDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить информацию о пользовательской роли",
            description = "В ответе возвращается UserRole.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> putUserRole(@PathVariable Long id, @RequestParam String name) {
        UserRoleDTO userRoleDTO = userRoleService.updateUserRole(id, name);
        return userRoleDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(userRoleDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить пользовательскую роль по id",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @DeleteMapping("/{id}")
    public void deleteUserRoleById(@PathVariable Long id) {
        userRoleService.deleteUserRole(id);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить пользовательскую роль по названию",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @DeleteMapping
    public void deleteUserRoleByName(@RequestParam String name) {
        userRoleService.deleteUserRole(name);
    }
}
