package com.example.CurrencyExchange.jwt;

import com.example.CurrencyExchange.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWTCore jwtCore;
    @Autowired
    private UserDetailsServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String login = null;
        UserDetails userDetails;
        UsernamePasswordAuthenticationToken auth;
        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer")) {
                jwt = headerAuth.substring(7);
            }
            if (jwt != null){
                try{
                    login = jwtCore.extractLogin(jwt);
                } catch (ExpiredJwtException e){
                    System.out.println("Can't get login(((");
                }
                if (login != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    userDetails = userService.loadUserByUsername(login);
                    auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        filterChain.doFilter(request, response);
    }
}
