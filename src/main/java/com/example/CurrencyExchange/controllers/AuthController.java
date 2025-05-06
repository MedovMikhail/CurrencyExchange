package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.UserDTO;
import com.example.CurrencyExchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO){
        String token = userService.register(userDTO);
        return token == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO){
        String token = userService.login(userDTO);
        return token == null ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED): ResponseEntity.ok(token);
    }
}
