package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.UserDTO;
import com.example.CurrencyExchange.entities.User;
import com.example.CurrencyExchange.entities.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements Mapper<User, UserDTO>{

    @Override
    public User fromDTOToEntity(UserDTO from) {
        if (from == null) return null;
        User to = new User();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setPhone(from.getPhone());
        to.setEmail(from.getEmail());
        to.setPassword(from.getPassword());
        to.setRole(new UserRole());
        to.getRole().setId(from.getRoleId());
        return to;
    }

    @Override
    public UserDTO fromEntityToDTO(User from) {
        if (from == null) return null;
        UserDTO to = new UserDTO();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setPhone(from.getPhone());
        to.setEmail(from.getEmail());
        to.setPassword(from.getPassword());
        to.setRoleId(from.getRole().getId());
        return to;
    }
}
