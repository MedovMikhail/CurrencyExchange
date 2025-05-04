package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.repositories.UserRoleRepository;
import com.example.CurrencyExchange.utils.mapping.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRoleDTO> getUserRoles() {
        return userRoleRepository.findAll().stream()
                .map(userRoleMapper::fromEntityToDTO)
                .toList();
    }

//    public UserRoleDTO getUserRole(long id) {
//
//    }
}
