package com.example.CurrencyExchange.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashRegister {

    @Id
    @SequenceGenerator(name = "cash_register_generator", sequenceName = "cash_register_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_register_generator")
    private Long id;

    @Column(nullable = false)
    private String address;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "cashRegister")
    private List<StoredCurrency> storedCurrencies;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "cashRegister")
    private List<ExchangeCurrency> exchangeCurrencies;
}
