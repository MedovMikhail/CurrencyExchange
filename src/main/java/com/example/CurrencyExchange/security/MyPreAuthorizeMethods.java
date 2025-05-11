package com.example.CurrencyExchange.security;

import com.example.CurrencyExchange.entities.UserRole;
import org.springframework.stereotype.Component;

@Component
public class MyPreAuthorizeMethods {
    public boolean isAdmin(UserRole role) {
        return role.getName().equals("ADMIN");
    }
}
