package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public List<UserRoleDTO> getUserRoles() {
        return userRoleService.getUserRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getUserRole(@PathVariable Long id) {
        UserRoleDTO userRoleDTO = userRoleService.getUserRole(id);
        return userRoleDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(userRoleDTO);
    }

    @PostMapping
    public ResponseEntity<UserRoleDTO> postUserRole(@RequestParam String name) {
        UserRoleDTO userRoleDTO = userRoleService.addUserRole(name);
        return userRoleDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(userRoleDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> putUserRole(@PathVariable Long id, @RequestParam String name) {
        UserRoleDTO userRoleDTO = userRoleService.updateUserRole(id, name);
        return userRoleDTO == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(userRoleDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUserRoleById(@PathVariable Long id) {
        userRoleService.deleteUserRole(id);
    }

    @DeleteMapping
    public void deleteUserRoleByName(@RequestParam String name) {
        userRoleService.deleteUserRole(name);
    }
}
