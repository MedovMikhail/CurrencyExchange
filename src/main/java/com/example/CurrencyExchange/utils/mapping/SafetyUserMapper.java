package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.SafetyUserDTO;
import com.example.CurrencyExchange.entities.User;
import com.example.CurrencyExchange.entities.UserRole;
import org.springframework.stereotype.Service;

@Service
public class SafetyUserMapper implements Mapper<User, SafetyUserDTO> {
    @Override
    public User fromDTOToEntity(SafetyUserDTO from) {
        if (from == null) return null;
        User to = new User();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setPhone(from.getPhone());
        to.setEmail(from.getEmail());
        to.setRole(new UserRole());
        to.getRole().setId(from.getRoleId());
        return to;
    }

    @Override
    public SafetyUserDTO fromEntityToDTO(User from) {
        if (from == null) return null;
        SafetyUserDTO to = new SafetyUserDTO();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setPhone(from.getPhone());
        to.setEmail(from.getEmail());
        to.setRoleId(from.getRole().getId());
        return to;
    }
}
