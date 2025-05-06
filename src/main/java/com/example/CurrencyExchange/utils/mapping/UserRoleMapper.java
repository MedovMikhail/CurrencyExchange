package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.entities.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserRoleMapper implements Mapper<UserRole, UserRoleDTO> {

    @Override
    public UserRole fromDTOToEntity(UserRoleDTO from) {
        if (from == null) return null;
        UserRole to = new UserRole();
        to.setId(from.getId());
        to.setName(from.getName());
        return to;
    }

    @Override
    public UserRoleDTO fromEntityToDTO(UserRole from) {
        if (from == null) return null;
        UserRoleDTO to = new UserRoleDTO();
        to.setId(from.getId());
        to.setName(from.getName());
        return to;
    }
}
