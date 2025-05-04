package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.CashRegisterDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import org.springframework.stereotype.Service;

@Service
public class CashRegisterMapper implements Mapper<CashRegister, CashRegisterDTO> {

    @Override
    public CashRegister fromDTOToEntity(CashRegisterDTO from) {
        if (from == null) return null;
        CashRegister to = new CashRegister();
        to.setId(from.getId());
        to.setAddress(from.getAddress());
        return to;
    }

    @Override
    public CashRegisterDTO fromEntityToDTO(CashRegister from) {
        if (from == null) return null;
        CashRegisterDTO to = new CashRegisterDTO();
        to.setId(from.getId());
        to.setAddress(from.getAddress());
        return to;
    }
}
