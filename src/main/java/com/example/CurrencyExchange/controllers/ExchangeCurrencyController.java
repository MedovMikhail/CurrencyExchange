package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.services.ExchangeCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @Operation(summary = "Запросить список страницы с операциями по обмену валюты",
            description = "В ответе возвращается список ExchangeCurrency.")
    @GetMapping("/page")
    public List<ExchangeCurrencyDTO> getAllPagingExchangeCurrencies(@RequestParam @Validated int pageNumber, @RequestParam @Validated int pageSize) {
        return exchangeCurrencyService.getExchangeCurrencies(pageNumber, pageSize);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить все операции по обмену валюты конкретных валют",
            description = "В ответе возвращается список ExchangeCurrency.")
    @GetMapping("/baseCode/{baseCode}/targetCode/{targetCode}")
    public List<ExchangeCurrencyDTO> getAllExchangeCurrenciesByBaseAndTargetCode(@PathVariable(required = false) String baseCode, @PathVariable(required = false) String targetCode) {
        return exchangeCurrencyService.getExchangeCurrenciesByBaseAndTargetCurrencyCode(baseCode, targetCode);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить все операции по обмену валюты в определенном интервале времени",
            description = "В ответе возвращается список ExchangeCurrency.")
    @GetMapping("/startDate/{startDate}/endDate/{endDate}")
    public List<ExchangeCurrencyDTO> getAllExchangeCurrenciesByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        return exchangeCurrencyService.getExchangeCurrenciesByDate(startDate, endDate);
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
    @Operation(summary = "Удалить операцию по обмену валюты",
            description = "В ответе возвращается ничего.")
    @PreAuthorize("@myPreAuthorizeMethods.isAdmin(authentication.principal.role)")
    @DeleteMapping("/{id}")
    public void deleteExchangeCurrency(@PathVariable Long id) {
        exchangeCurrencyService.deleteExchangeCurrency(id);
    }
}
