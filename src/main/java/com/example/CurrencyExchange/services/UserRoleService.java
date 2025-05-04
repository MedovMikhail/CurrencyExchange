package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.entities.UserRole;
import com.example.CurrencyExchange.repositories.UserRoleRepository;
import com.example.CurrencyExchange.utils.mapping.UserRoleMapper;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserRoleDTO getUserRole(long id) {
        return userRoleMapper.fromEntityToDTO(userRoleRepository.findById(id).orElse(null));
    }

    public UserRoleDTO addUserRole(String roleName) {
        UserRole role = new UserRole();
        role.setRole(roleName.toUpperCase());
        try {
            role = userRoleRepository.save(role);
            return userRoleMapper.fromEntityToDTO(role);
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    public UserRoleDTO updateUserRole(long id, String roleName) {
        UserRole role = userRoleRepository.findById(id).orElse(null);
        if (role == null) return null;
        role.setRole(roleName.toUpperCase());
        try {
            role = userRoleRepository.save(role);
        } catch (NonUniqueResultException e) {
            return null;
        }
        return userRoleMapper.fromEntityToDTO(role);
    }

    public void deleteUserRole(long id) {
        userRoleRepository.deleteById(id);
    }

    public void deleteUserRole(String roleName) {
        userRoleRepository.deleteByRole(roleName.toUpperCase());
    }
}
