package com.example.CurrencyExchange.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrency {

    @Id
    @SequenceGenerator(name = "exchange_currency_generator", sequenceName = "exchange_currency_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_currency_generator")
    private Long id;

    @Column(nullable = false, scale = 3, precision = 38)
    private BigDecimal countBaseCash;

    @Column(nullable = false, scale = 3, precision = 38)
    private BigDecimal countTargetCash;

    @Column(nullable = false, scale = 8, precision = 20)
    private BigDecimal exchangeRate;

    @Column(nullable = false, scale = 8, precision = 20)
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
