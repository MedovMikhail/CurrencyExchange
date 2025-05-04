package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.entities.Currency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyMapper implements Mapper<Currency, CurrencyDTO>{

    @Override
    public Currency fromDTOToEntity(CurrencyDTO from) {
        Currency to = new Currency();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setCode(from.getCode());
        return to;
    }

    @Override
    public CurrencyDTO fromEntityToDTO(Currency from) {
        CurrencyDTO to = new CurrencyDTO();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setCode(from.getCode());
        return to;
    }
}
