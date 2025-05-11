package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import com.example.CurrencyExchange.entities.Currency;
import com.example.CurrencyExchange.entities.StoredCurrency;
import org.springframework.stereotype.Service;

@Service
public class StoredCurrencyMapper implements Mapper<StoredCurrency, StoredCurrencyDTO> {

    @Deprecated
    @Override
    public StoredCurrency fromDTOToEntity(StoredCurrencyDTO from) {
        if (from == null) return null;
        StoredCurrency to = new StoredCurrency();
        to.setId(from.getId());
        to.setCount(from.getCount());
        to.setCashRegister(new CashRegister());
        to.getCashRegister().setId(from.getCashRegisterId());
        return to;
    }

    public StoredCurrency fromDTOToEntity(StoredCurrencyDTO from, Long currencyId) {
        if (from == null) return null;
        StoredCurrency to = new StoredCurrency();
        to.setId(from.getId());
        to.setCount(from.getCount());
        to.setCurrency(new Currency());
        to.getCurrency().setId(currencyId);
        to.setCashRegister(new CashRegister());
        to.getCashRegister().setId(from.getCashRegisterId());
        return to;
    }

    @Override
    public StoredCurrencyDTO fromEntityToDTO(StoredCurrency from) {
        if (from == null) return null;
        StoredCurrencyDTO to = new StoredCurrencyDTO();
        to.setId(from.getId());
        to.setCount(from.getCount());
        to.setCurrencyCode(from.getCurrency().getCode());
        to.setCashRegisterId(from.getCashRegister().getId());
        return to;
    }
}
