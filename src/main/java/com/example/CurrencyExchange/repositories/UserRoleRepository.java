package com.example.CurrencyExchange.repositories;

import com.example.CurrencyExchange.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    void deleteByRole(String role);
}
