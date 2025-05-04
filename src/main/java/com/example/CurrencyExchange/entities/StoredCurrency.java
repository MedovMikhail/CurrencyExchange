package com.example.CurrencyExchange.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredCurrency {

    @Id
    @SequenceGenerator(name = "stored_currency_generator", sequenceName = "stored_currency_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stored_currency_generator")
    private Long id;

    @Column(nullable = false)
    private BigDecimal count;

    @ManyToOne(targetEntity = Currency.class, fetch = FetchType.LAZY)
    @JoinColumn(name="currency_id")
    private Currency currency;

    @ManyToOne(targetEntity = CashRegister.class, fetch = FetchType.LAZY)
    @JoinColumn(name="cash_register_id")
    private CashRegister cashRegister;
}
