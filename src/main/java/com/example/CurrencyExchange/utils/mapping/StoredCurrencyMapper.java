package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import com.example.CurrencyExchange.entities.Currency;
import com.example.CurrencyExchange.entities.StoredCurrency;
import org.springframework.stereotype.Service;

@Service
public class StoredCurrencyMapper implements Mapper<StoredCurrency, StoredCurrencyDTO> {

    @Override
    public StoredCurrency fromDTOToEntity(StoredCurrencyDTO from) {
        StoredCurrency to = new StoredCurrency();
        to.setId(from.getId());
        to.setCount(from.getCount());
        to.setCurrency(new Currency());
        to.getCurrency().setId(from.getCurrencyId());
        to.setCashRegister(new CashRegister());
        to.getCashRegister().setId(from.getCashRegisterId());
        return to;
    }

    @Override
    public StoredCurrencyDTO fromEntityToDTO(StoredCurrency from) {
        StoredCurrencyDTO to = new StoredCurrencyDTO();
        to.setId(from.getId());
        to.setCount(from.getCount());
        to.setCurrencyId(from.getCurrency().getId());
        to.setCashRegisterId(from.getCashRegister().getId());
        return to;
    }
}
