package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CurrencyDTO getCurrencyById(@PathVariable long id) {
        return currencyService.getCurrency(id);
    }

    @GetMapping("/code")
    public CurrencyDTO getCurrencyByCode(@RequestParam String code) {
        return currencyService.getCurrency(code);
    }

    @PostMapping
    public CurrencyDTO postCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return currencyService.addCurrency(currencyDTO);
    }

    @PutMapping("/{id}")
    public CurrencyDTO putCurrency(@PathVariable long id, @RequestBody CurrencyDTO currencyDTO) {
        return currencyService.updateCurrency(id, currencyDTO);
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
