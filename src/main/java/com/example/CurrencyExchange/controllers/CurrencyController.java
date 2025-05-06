package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDTO> getCurrencies() {
        return currencyService.getCurrencies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getCurrencyById(@PathVariable long id) {
        CurrencyDTO currencyDTO = currencyService.getCurrency(id);
        return currencyDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(currencyDTO);
    }

    @GetMapping("/code")
    public ResponseEntity<CurrencyDTO> getCurrencyByCode(@RequestParam String code) {
        CurrencyDTO currencyDTO = currencyService.getCurrency(code);
        return currencyDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(currencyDTO);
    }

    @PostMapping
    public ResponseEntity<CurrencyDTO> postCurrency(@RequestBody CurrencyDTO currencyDTO) {
        currencyDTO = currencyService.addCurrency(currencyDTO);
        return currencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(currencyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDTO> putCurrency(@PathVariable long id, @RequestBody CurrencyDTO currencyDTO) {
        currencyDTO = currencyService.updateCurrency(id, currencyDTO);
        return currencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(currencyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCurrencyById(@PathVariable long id) {
        currencyService.deleteCurrency(id);
    }

    @DeleteMapping
    public void deleteCurrencyByCode(@RequestParam String code) {
        currencyService.deleteCurrency(code);
    }
}
