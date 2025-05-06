package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.Currency;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByCode(String code);
    @Transactional
    void deleteByCode(String code);
}
