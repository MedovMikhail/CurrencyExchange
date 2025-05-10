package com.example.CurrencyExchange.utils.mapping;

import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.dto.kafka.ExchangedCurrencyDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import com.example.CurrencyExchange.entities.Currency;
import com.example.CurrencyExchange.entities.ExchangeCurrency;
import com.example.CurrencyExchange.entities.User;
import org.springframework.stereotype.Service;

@Service
public class ExchangeCurrencyMapper {

    public ExchangeCurrency fromDTOToEntity(ExchangeCurrencyDTO from, long baseCurId, long targetCurId) {
        if (from == null) return null;
        ExchangeCurrency to = new ExchangeCurrency();
        to.setId(from.getId());
        to.setExchangeRate(from.getExchangeRate());
        to.setCountBaseCash(from.getCountBaseCash());
        to.setCountTargetCash(from.getCountTargetCash());
        to.setDateOfExchange(from.getDateOfExchange());
        to.setBaseCurrency(new Currency());
        to.getBaseCurrency().setId(baseCurId);
        to.setTargetCurrency(new Currency());
        to.getTargetCurrency().setId(targetCurId);
        to.setCashRegister(new CashRegister());
        to.getCashRegister().setId(from.getCashRegisterId());
        to.setUser(new User());
        to.getUser().setId(from.getUserId());
        return to;
    }

    public ExchangeCurrencyDTO fromEntityToDTO(ExchangeCurrency from) {
        if (from == null) return null;
        ExchangeCurrencyDTO to = new ExchangeCurrencyDTO();
        to.setId(from.getId());
        to.setExchangeRate(from.getExchangeRate());
        to.setCountBaseCash(from.getCountBaseCash());
        to.setCountTargetCash(from.getCountTargetCash());
        to.setDateOfExchange(from.getDateOfExchange());
        to.setBaseCurrencyCode(from.getBaseCurrency().getCode());
        to.setTargetCurrencyCode(from.getTargetCurrency().getCode());
        to.setCashRegisterId(from.getCashRegister().getId());
        to.setUserId(from.getUser().getId());
        return to;
    }

    public ExchangeCurrencyDTO fromEntityToDTO(ExchangeCurrency from, String baseCurrencyCode, String targetCurrencyCode) {
        if (from == null) return null;
        ExchangeCurrencyDTO to = new ExchangeCurrencyDTO();
        to.setId(from.getId());
        to.setExchangeRate(from.getExchangeRate());
        to.setCountBaseCash(from.getCountBaseCash());
        to.setCountTargetCash(from.getCountTargetCash());
        to.setDateOfExchange(from.getDateOfExchange());
        to.setBaseCurrencyCode(baseCurrencyCode);
        to.setTargetCurrencyCode(targetCurrencyCode);
        to.setCashRegisterId(from.getCashRegister().getId());
        to.setUserId(from.getUser().getId());
        return to;
    }

    public ExchangeCurrencyDTO fromExchangedToExchange(ExchangeCurrencyDTO to, ExchangedCurrencyDTO from) {
        to.setCountTargetCash(
                from.getTargetStoredCurrencyDiff()
        );
        to.setDateOfExchange(from.getDateOfExchange());
        return to;
    }
}
