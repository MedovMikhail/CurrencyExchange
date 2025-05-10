package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.StoredCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface StoredCurrencyRepository extends JpaRepository<StoredCurrency, Long> {

    @Query(value = "select sc.count from StoredCurrency as sc where sc.cashRegister.id = :cashRegisterId and sc.currency.code = :code")
    BigDecimal getStoredCurrencyCountByCode(@Param(value="cashRegisterId") Long cashRegisterId, @Param(value="code") String code);
}
