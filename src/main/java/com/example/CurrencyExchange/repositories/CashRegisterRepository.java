package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {

}
