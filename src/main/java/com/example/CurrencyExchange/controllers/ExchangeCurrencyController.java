package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.services.ExchangeCurrencyService;
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

    @GetMapping
    public List<ExchangeCurrencyDTO> getAllExchangeCurrencies() {
        return exchangeCurrencyService.getExchangeCurrencies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeCurrencyDTO> getExchangeCurrency(@PathVariable  long id) {
        ExchangeCurrencyDTO exchangeCurrencyDTO = exchangeCurrencyService.getExchangeCurrency(id);
        return exchangeCurrencyDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(exchangeCurrencyDTO);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<ExchangeCurrencyDTO> postExchangeCurrency(
            @RequestParam long userId, @RequestParam long cashRegId,
            @RequestBody ExchangeCurrencyDTO exchangeCurrencyDTO) {
        exchangeCurrencyDTO = exchangeCurrencyService.addExchangeCurrency(userId, cashRegId, exchangeCurrencyDTO);
        return exchangeCurrencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(exchangeCurrencyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExchangeCurrencyDTO> putExchangeCurrency(@PathVariable long id, @RequestBody BigDecimal baseCurCount) {
        ExchangeCurrencyDTO exchangeCurrencyDTO = exchangeCurrencyService.updateExchangeCurrency(id, baseCurCount);
        return exchangeCurrencyDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(exchangeCurrencyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteExchangeCurrency(@PathVariable long id) {
        exchangeCurrencyService.deleteExchangeCurrency(id);
    }
}
