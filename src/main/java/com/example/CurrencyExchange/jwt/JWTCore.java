package com.example.CurrencyExchange.jwt;

import com.example.CurrencyExchange.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTCore {
    @Value("${cargo.app.secret}")
    private String secret;
    @Value("${cargo.app.lifetime}")
    private int lifetime;

    public String generateToken(Authentication authentication) {
        // получаем пользователя по принциплу (principal = аутентифицированных пользователь)
        User userDetails = (User) authentication.getPrincipal();
        // маппа информации, которую положим в токен
        HashMap<String, String> payload = new HashMap<>();
        // добавляем id
        payload.put("id", userDetails.getId().toString());
        // и роль
        payload.put("role", userDetails.getRole().getName());
        //собираем токен
        return Jwts.builder().subject(userDetails.getEmail())
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))                     // дата создания токена
                .expiration(new Date(System.currentTimeMillis() + 120L * lifetime)) // дата, когда токен исчерпает себя
                .signWith(generateKey())                                            // делаем подпись токена
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decode = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractLogin(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
