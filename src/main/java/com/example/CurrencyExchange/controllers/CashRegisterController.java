package com.example.CurrencyExchange.controllers;


import com.example.CurrencyExchange.dto.CashRegisterDTO;
import com.example.CurrencyExchange.services.CashRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registers")
public class CashRegisterController {

    @Autowired
    private CashRegisterService cashRegisterService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить список всех касс",
            description = "В ответе возвращается список CashRegister.")
    @GetMapping
    public List<CashRegisterDTO> getCashRegisters() {
        return cashRegisterService.getCashRegisters();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запросить кассу по id",
            description = "В ответе возвращается CashRegister.")
    @GetMapping("/{id}")
    public ResponseEntity<CashRegisterDTO> getCashRegisterById(@PathVariable Long id) {
        CashRegisterDTO cashRegisterDTO = cashRegisterService.getCashRegister(id);
        return cashRegisterDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(cashRegisterDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавить кассу",
            description = "В ответе возвращается CashRegister.")
    @PostMapping
    public ResponseEntity<CashRegisterDTO> postCashRegister(@RequestBody CashRegisterDTO cashRegisterDTO) {
        cashRegisterDTO = cashRegisterService.addCashRegister(cashRegisterDTO);
        return cashRegisterDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(cashRegisterDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить информацию о кассе",
            description = "В ответе возвращается CashRegister.")
    @PutMapping("/{id}")
    public ResponseEntity<CashRegisterDTO> putCashRegister(@PathVariable Long id, @RequestBody CashRegisterDTO cashRegisterDTO) {
        cashRegisterDTO = cashRegisterService.updateCashRegister(id, cashRegisterDTO);
        return cashRegisterDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(cashRegisterDTO);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить кассу по id",
            description = "В ответе возвращается ничего.")
    @DeleteMapping("/{id}")
    public void deleteCashRegister(@PathVariable Long id) {
            cashRegisterService.deleteCashRegister(id);
    }
}
