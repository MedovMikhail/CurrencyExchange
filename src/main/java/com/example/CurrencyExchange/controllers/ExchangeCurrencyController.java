package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.services.ExchangeCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/exchange-currencies")
public class ExchangeCurrencyController {

    @Autowired
    private ExchangeCurrencyService exchangeCurrencyService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить список всех операций по обмену валюты",
            description = "В ответе возвращается список ExchangeCurrency.")
    @GetMapping
    public List<ExchangeCurrencyDTO> getAllExchangeCurrencies() {
        return exchangeCurrencyService.getExchangeCurrencies();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить операцию по обмену валюты по id",
            description = "В ответе возвращается ExchangeCurrency.")
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeCurrencyDTO> getExchangeCurrency(@PathVariable  Long id) {
        ExchangeCurrencyDTO exchangeCurrencyDTO = exchangeCurrencyService.getExchangeCurrency(id);
        return exchangeCurrencyDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(exchangeCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавить операцию по обмену валюты",
            description = "В ответе возвращается ExchangeCurrency.")
    @PreAuthorize("#userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<ExchangeCurrencyDTO> postExchangeCurrency(
            @RequestParam Long userId, @RequestParam Long cashRegId,
            @RequestBody ExchangeCurrencyDTO exchangeCurrencyDTO) {
        exchangeCurrencyDTO = exchangeCurrencyService.addExchangeCurrency(userId, cashRegId, exchangeCurrencyDTO);
        return exchangeCurrencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(exchangeCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить информацию об операции по обмену валюты",
            description = "В ответе возвращается ExchangeCurrency.")
    @PutMapping("/{id}")
    public ResponseEntity<ExchangeCurrencyDTO> putExchangeCurrency(@PathVariable Long id, @RequestBody BigDecimal baseCurCount) {
        ExchangeCurrencyDTO exchangeCurrencyDTO = exchangeCurrencyService.updateExchangeCurrency(id, baseCurCount);
        return exchangeCurrencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(exchangeCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить операцию по обмену валюты",
            description = "В ответе возвращается ничего.")
    @DeleteMapping("/{id}")
    public void deleteExchangeCurrency(@PathVariable Long id) {
        exchangeCurrencyService.deleteExchangeCurrency(id);
    }
}
