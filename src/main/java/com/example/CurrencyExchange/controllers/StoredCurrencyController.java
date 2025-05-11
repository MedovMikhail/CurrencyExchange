package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.services.StoredCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Хранимые валюты")
@RestController
@RequestMapping("stored-currencies")
public class StoredCurrencyController {

    @Autowired
    private StoredCurrencyService storedCurrencyService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить список всех хранимых валют",
            description = "В ответе возвращается список StoredCurrency.")
    @GetMapping
    public ResponseEntity<List<StoredCurrencyDTO>> getStoredCurrencies() {
        return ResponseEntity.ok(storedCurrencyService.getStoredCurrencies());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить страницу со списком хранимых валют",
            description = "В ответе возвращается список StoredCurrency.")
    @GetMapping("/page")
    public ResponseEntity<List<StoredCurrencyDTO>> getPagingStoredCurrencies(
            @RequestParam @Validated int pageNumber, @RequestParam @Validated int pageSize
    ) {
        return ResponseEntity.ok(storedCurrencyService.getStoredCurrencies(pageNumber, pageSize));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить хранимую валюту по id",
            description = "В ответе возвращается StoredCurrency.")
    @GetMapping("/{id}")
    public ResponseEntity<StoredCurrencyDTO> getStoredCurrencyById(@PathVariable Long id) {
        StoredCurrencyDTO storedCurrencyDTO = storedCurrencyService.getStoredCurrency(id);
        return storedCurrencyDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавить хранимую валюту",
            description = "В ответе возвращается StoredCurrency.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PostMapping
    public ResponseEntity<StoredCurrencyDTO> postStoredCurrency(
            @RequestParam Long curId,
            @RequestParam Long cashRegId,
            @RequestBody StoredCurrencyDTO storedCurrencyDTO
    ) {
        storedCurrencyDTO = storedCurrencyService.addStoredCurrency(curId, cashRegId, storedCurrencyDTO);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить информацию о хранимой валюте",
            description = "В ответе возвращается StoredCurrency.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PutMapping("/{id}")
    public ResponseEntity<StoredCurrencyDTO> putStoredCurrency(@PathVariable Long id, @RequestBody StoredCurrencyDTO storedCurrencyDTO) {
        storedCurrencyDTO = storedCurrencyService.updateStoredCurrency(id, storedCurrencyDTO);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменить (прибавить/убавить) количество хранимой валюты",
            description = "В ответе возвращается StoredCurrency.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PutMapping("/{id}/change")
    public ResponseEntity<StoredCurrencyDTO> changeCountStoredCurrency(@PathVariable Long id, @RequestParam BigDecimal count, @RequestParam boolean isAdd) {
        StoredCurrencyDTO storedCurrencyDTO = storedCurrencyService.changeCountStoredCurrency(id, count, isAdd);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Пересчитать все валюты, в зависимости от их курса в кассе",
            description = "В ответе возвращается список StoredCurrency.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PutMapping
    public ResponseEntity<List<StoredCurrencyDTO>> recountCurrency(@RequestParam Long cashRegId) {
        List<StoredCurrencyDTO> storedCurrenciesDTO = storedCurrencyService.recountCurrencies(cashRegId);
        return storedCurrenciesDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrenciesDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить хранимую валюту по id",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @DeleteMapping("/{id}")
    public void deleteStoredCurrency(@PathVariable Long id) {
        storedCurrencyService.deleteStoredCurrency(id);
    }
}
