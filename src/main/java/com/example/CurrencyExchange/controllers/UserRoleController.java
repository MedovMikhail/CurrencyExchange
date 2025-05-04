package com.example.CurrencyExchange.controllers;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserRoleDTO getUserRole(@PathVariable long id) {
        return userRoleService.getUserRole(id);
    }

    @PostMapping
    public UserRoleDTO postUserRole(@RequestParam String name) {
        System.out.println(1);
        return userRoleService.addUserRole(name);
    }

    @PutMapping("/{id}")
    public UserRoleDTO putUserRole(@PathVariable long id, @RequestParam String name) {
        return userRoleService.updateUserRole(id, name);
    }

    @DeleteMapping("/{id}")
    public void deleteUserRoleById(@PathVariable long id) {
        userRoleService.deleteUserRole(id);
    }

    @DeleteMapping
    public void deleteUserRoleByName(@RequestParam String name) {
        userRoleService.deleteUserRole(name);
    }
}
