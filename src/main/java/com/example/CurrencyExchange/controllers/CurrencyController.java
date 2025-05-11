package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.services.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить список всех валют",
            description = "В ответе возвращается список Currency.")
    @GetMapping
    public List<CurrencyDTO> getCurrencies() {
        return currencyService.getCurrencies();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить валюту по id",
            description = "В ответе возвращается Currency.")
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getCurrencyById(@PathVariable Long id) {
        CurrencyDTO currencyDTO = currencyService.getCurrency(id);
        return currencyDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(currencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить валюту по её коду",
            description = "В ответе возвращается Currency.")
    @GetMapping("/code")
    public ResponseEntity<CurrencyDTO> getCurrencyByCode(@RequestParam String code) {
        CurrencyDTO currencyDTO = currencyService.getCurrency(code);
        return currencyDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(currencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавить валюту",
            description = "В ответе возвращается Currency.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PostMapping
    public ResponseEntity<CurrencyDTO> postCurrency(@RequestBody CurrencyDTO currencyDTO) {
        currencyDTO = currencyService.addCurrency(currencyDTO);
        return currencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(currencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить информацию о валюте",
            description = "В ответе возвращается Currency.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDTO> putCurrency(@PathVariable Long id, @RequestBody CurrencyDTO currencyDTO) {
        currencyDTO = currencyService.updateCurrency(id, currencyDTO);
        return currencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(currencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить валюту по id",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @DeleteMapping("/{id}")
    public void deleteCurrencyById(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить валюту по её коду",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @DeleteMapping
    public void deleteCurrencyByCode(@RequestParam String code) {
        currencyService.deleteCurrency(code);
    }
}
