package com.example.CurrencyExchange.controllers;


import com.example.CurrencyExchange.dto.CashRegisterDTO;
import com.example.CurrencyExchange.services.CashRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registers")
public class CashRegisterController {

    @Autowired
    private CashRegisterService cashRegisterService;

    @GetMapping
    public List<CashRegisterDTO> getCashRegisters() {
        return cashRegisterService.getCashRegisters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashRegisterDTO> getCashRegisterById(@PathVariable Long id) {
        CashRegisterDTO cashRegisterDTO = cashRegisterService.getCashRegister(id);
        return cashRegisterDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(cashRegisterDTO);
    }

    @PostMapping
    public ResponseEntity<CashRegisterDTO> postCashRegister(@RequestBody CashRegisterDTO cashRegisterDTO) {
        cashRegisterDTO = cashRegisterService.addCashRegister(cashRegisterDTO);
        return cashRegisterDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(cashRegisterDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CashRegisterDTO> putCashRegister(@PathVariable Long id, @RequestBody CashRegisterDTO cashRegisterDTO) {
        cashRegisterDTO = cashRegisterService.updateCashRegister(id, cashRegisterDTO);
        return cashRegisterDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(cashRegisterDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCashRegister(@PathVariable Long id) {
            cashRegisterService.deleteCashRegister(id);
    }
}
