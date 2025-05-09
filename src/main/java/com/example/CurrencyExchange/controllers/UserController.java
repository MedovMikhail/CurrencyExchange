package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.SafetyUserDTO;
import com.example.CurrencyExchange.dto.UserDTO;
import com.example.CurrencyExchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<SafetyUserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SafetyUserDTO> getUserById(@PathVariable Long id) {
        SafetyUserDTO safetyUserDTO = userService.getUser(id);
        return safetyUserDTO == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(safetyUserDTO);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<SafetyUserDTO> putUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        SafetyUserDTO safetyUserDTO = userService.updateUser(id, userDTO);
        return safetyUserDTO == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(safetyUserDTO);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
