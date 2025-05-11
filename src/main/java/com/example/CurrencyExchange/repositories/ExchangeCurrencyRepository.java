package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.ExchangeCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ExchangeCurrencyRepository extends JpaRepository<ExchangeCurrency, Long> {
    List<ExchangeCurrency> findByBaseCurrencyCodeContainingIgnoreCaseAndTargetCurrencyCodeContainingIgnoreCase(
            String baseCurrencyCode, String targetCurrencyCode
    );
    List<ExchangeCurrency> findAllByDateOfExchangeBetween(ZonedDateTime startDate, ZonedDateTime endDate);
}
