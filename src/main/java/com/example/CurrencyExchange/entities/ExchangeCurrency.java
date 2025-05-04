package com.example.CurrencyExchange.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal countBaseCash;

    @Column(nullable = false)
    private BigDecimal countTargetCash;

    @Column(nullable = false)
    private BigDecimal exchangeRate;

    @Column(nullable = false)
    private ZonedDateTime dateOfExchange;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(targetEntity = Currency.class, fetch = FetchType.LAZY)
    @JoinColumn(name="base_currency_id")
    private Currency baseCurrency;

    @ManyToOne(targetEntity = Currency.class, fetch = FetchType.LAZY)
    @JoinColumn(name="target_currency_id")
    private Currency targetCurrency;

    @ManyToOne(targetEntity = CashRegister.class, fetch = FetchType.LAZY)
    @JoinColumn(name="cash_register_id")
    private CashRegister cashRegister;
}
