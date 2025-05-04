package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import com.example.CurrencyExchange.entities.Currency;
import com.example.CurrencyExchange.entities.ExchangeCurrency;
import com.example.CurrencyExchange.entities.User;
import org.springframework.stereotype.Service;

@Service
public class ExchangeCurrencyMapper implements Mapper<ExchangeCurrency, ExchangeCurrencyDTO>{

    @Override
    public ExchangeCurrency fromDTOToEntity(ExchangeCurrencyDTO from) {
        ExchangeCurrency to = new ExchangeCurrency();
        to.setId(from.getId());
        to.setExchangeRate(from.getExchangeRate());
        to.setCountBaseCash(from.getCountBaseCash());
        to.setCountTargetCash(from.getCountTargetCash());
        to.setDateOfExchange(from.getDateOfExchange());
        to.setBaseCurrency(new Currency());
        to.getBaseCurrency().setId(from.getBaseCurrencyId());
        to.setTargetCurrency(new Currency());
        to.getTargetCurrency().setId(from.getTargetCurrencyId());
        to.setCashRegister(new CashRegister());
        to.getCashRegister().setId(from.getCashRegisterId());
        to.setUser(new User());
        to.getUser().setId(from.getUserId());
        return to;
    }

    @Override
    public ExchangeCurrencyDTO fromEntityToDTO(ExchangeCurrency from) {
        ExchangeCurrencyDTO to = new ExchangeCurrencyDTO();
        to.setId(from.getId());
        to.setExchangeRate(from.getExchangeRate());
        to.setCountBaseCash(from.getCountBaseCash());
        to.setCountTargetCash(from.getCountTargetCash());
        to.setDateOfExchange(from.getDateOfExchange());
        to.setBaseCurrencyId(from.getBaseCurrency().getId());
        to.setTargetCurrencyId(from.getTargetCurrency().getId());
        to.setCashRegisterId(from.getCashRegister().getId());
        to.setUserId(from.getUser().getId());
        return to;
    }
}
