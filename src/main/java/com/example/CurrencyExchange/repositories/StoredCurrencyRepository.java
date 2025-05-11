package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.StoredCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoredCurrencyRepository extends JpaRepository<StoredCurrency, Long> {

    List<StoredCurrency> findAllByOrderById();

    @Query(value = "select sc from StoredCurrency as sc where sc.cashRegister.id = :cashRegisterId and sc.currency.code = :code")
    Optional<StoredCurrency> getStoredCurrencyByCode(@Param(value="cashRegisterId") Long cashRegisterId, @Param(value="code") String code);
}
