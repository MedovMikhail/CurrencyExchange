package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.ExchangeCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeCurrencyRepository extends JpaRepository<ExchangeCurrency, Long> {

}
