package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.entities.UserRole;
import com.example.CurrencyExchange.repositories.UserRoleRepository;
import com.example.CurrencyExchange.utils.mapping.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRoleDTO> getUserRoles() {
        return userRoleRepository.findAll(Sort.by("id"))
                .stream()
                .map(userRoleMapper::fromEntityToDTO)
                .toList();
    }

    public List<UserRoleDTO> getUserRoles(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return userRoleRepository.findAll(pageable)
                .stream()
                .map(userRoleMapper::fromEntityToDTO)
                .toList();
    }

    public UserRoleDTO getUserRole(Long id) {
        return userRoleMapper.fromEntityToDTO(userRoleRepository.findById(id).orElse(null));
    }

    public UserRoleDTO getUserRole(String name) {
        return userRoleMapper.fromEntityToDTO(userRoleRepository.findByName(name.toUpperCase()));
    }

    public UserRoleDTO addUserRole(String roleName) {
        UserRole role = new UserRole();
        role.setName(roleName.toUpperCase());
        try {
            role = userRoleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return userRoleMapper.fromEntityToDTO(role);
    }

    public UserRoleDTO updateUserRole(Long id, String roleName) {
        UserRole role = userRoleRepository.findById(id).orElse(null);
        if (role == null) return null;
        role.setName(roleName.toUpperCase());
        try {
            role = userRoleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return userRoleMapper.fromEntityToDTO(role);
    }

    public void deleteUserRole(Long id) {
        userRoleRepository.deleteById(id);
    }

    public void deleteUserRole(String roleName) {
        userRoleRepository.deleteByName(roleName.toUpperCase());
    }
}
