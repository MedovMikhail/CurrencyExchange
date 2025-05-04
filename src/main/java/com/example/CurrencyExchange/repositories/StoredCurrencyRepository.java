package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.StoredCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoredCurrencyRepository extends JpaRepository<StoredCurrency, UUID> {

}
