package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByName(String name);
    @Transactional
    void deleteByName(String name);
}
