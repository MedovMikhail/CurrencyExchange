package com.example.CurrencyExchange.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal count;

    @ManyToOne(targetEntity = Currency.class, fetch = FetchType.LAZY)
    @JoinColumn(name="currency_id")
    private Currency currency;

    @ManyToOne(targetEntity = CashRegister.class, fetch = FetchType.LAZY)
    @JoinColumn(name="cash_register_id")
    private CashRegister cashRegister;
}
