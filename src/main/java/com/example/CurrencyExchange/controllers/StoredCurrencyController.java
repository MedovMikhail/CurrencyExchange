package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.services.StoredCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    @PutMapping("/{id}")
    public ResponseEntity<StoredCurrencyDTO> putStoredCurrency(@PathVariable Long id, @RequestBody StoredCurrencyDTO storedCurrencyDTO) {
        storedCurrencyDTO = storedCurrencyService.updateStoredCurrency(id, storedCurrencyDTO);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменить (прибавить/убавить) количество хранимой валюты",
            description = "В ответе возвращается StoredCurrency.")
    @PutMapping("/{id}/change")
    public ResponseEntity<StoredCurrencyDTO> changeCountStoredCurrency(@PathVariable Long id, @RequestParam BigDecimal count, @RequestParam boolean isAdd) {
        StoredCurrencyDTO storedCurrencyDTO = storedCurrencyService.changeCountStoredCurrency(id, count, isAdd);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить хранимую валюту по id",
            description = "В ответе возвращается ничего.")
    @DeleteMapping("/{id}")
    public void deleteStoredCurrency(@PathVariable Long id) {
        storedCurrencyService.deleteStoredCurrency(id);
    }
}
