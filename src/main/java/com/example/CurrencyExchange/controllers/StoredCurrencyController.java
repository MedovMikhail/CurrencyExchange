package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.services.StoredCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stored-currencies")
public class StoredCurrencyController {

    @Autowired
    private StoredCurrencyService storedCurrencyService;

    @GetMapping
    public ResponseEntity<List<StoredCurrencyDTO>> getStoredCurrencies() {
        return ResponseEntity.ok(storedCurrencyService.getStoredCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoredCurrencyDTO> getStoredCurrencyById(@PathVariable long id) {
        StoredCurrencyDTO storedCurrencyDTO = storedCurrencyService.getStoredCurrency(id);
        return storedCurrencyDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @PostMapping
    public ResponseEntity<StoredCurrencyDTO> postStoredCurrency(@RequestBody StoredCurrencyDTO storedCurrencyDTO) {
        storedCurrencyDTO = storedCurrencyService.createStoredCurrency(storedCurrencyDTO);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoredCurrencyDTO> putStoredCurrency(@PathVariable long id, @RequestBody StoredCurrencyDTO storedCurrencyDTO) {
        storedCurrencyDTO = storedCurrencyService.updateStoredCurrency(id, storedCurrencyDTO);
        return storedCurrencyDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(storedCurrencyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteStoredCurrency(@PathVariable long id) {
        storedCurrencyService.deleteStoredCurrency(id);
    }
}
