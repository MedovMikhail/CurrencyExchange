package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.dto.ExchangeCurrencyDTO;
import com.example.CurrencyExchange.entities.ExchangeCurrency;
import com.example.CurrencyExchange.repositories.ExchangeCurrencyRepository;
import com.example.CurrencyExchange.utils.mapping.ExchangeCurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ExchangeCurrencyService {

    @Autowired
    private ExchangeCurrencyMapper exchangeCurrencyMapper;
    @Autowired
    private ExchangeCurrencyRepository exchangeCurrencyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CashRegisterService cashRegisterService;
    @Autowired
    private CurrencyService currencyService;

    public List<ExchangeCurrencyDTO> getExchangeCurrencies(){
        return exchangeCurrencyRepository.findAll()
                .stream()
                .map(exchangeCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public ExchangeCurrencyDTO getExchangeCurrency(Long id) {
        return exchangeCurrencyMapper.fromEntityToDTO(
                exchangeCurrencyRepository.findById(id).orElse(null)
        );
    }

    public ExchangeCurrencyDTO addExchangeCurrency(
            Long userId, Long cashRegId,
            ExchangeCurrencyDTO exchangeCurrencyRequestDTO
    ) {
        /*
            if user and cashRegister exists
         */
        if (userService.getUser(userId) == null || cashRegisterService.getCashRegister(cashRegId) == null) return null;

        CurrencyDTO baseCurrencyDTO = currencyService.getCurrency(
                exchangeCurrencyRequestDTO.getBaseCurrencyCode()
        );
        CurrencyDTO targetCurrencyDTO = currencyService.getCurrency(
                exchangeCurrencyRequestDTO.getTargetCurrencyCode()
        );
        /*
            if baseCurrency and targetCurrency exists
         */
        if (baseCurrencyDTO == null || targetCurrencyDTO == null) return null;

        exchangeCurrencyRequestDTO.setUserId(userId);
        exchangeCurrencyRequestDTO.setCashRegisterId(cashRegId);

        ExchangeCurrency exchangeCurrency = exchangeCurrencyMapper.fromDTOToEntity(
                exchangeCurrencyRequestDTO, baseCurrencyDTO.getId(), targetCurrencyDTO.getId()
        );

        /*
            it's temporary, because need to do with application2 and application3
         */
        exchangeCurrency.setExchangeRate(BigDecimal.valueOf(81));
        exchangeCurrency.setCountTargetCash(
                exchangeCurrency.getCountBaseCash().divide(exchangeCurrency.getExchangeRate(), 2, RoundingMode.DOWN)
        );

        exchangeCurrency.setDateOfExchange(ZonedDateTime.now(ZoneId.of("Europe/Moscow")));

        try {
            exchangeCurrency = exchangeCurrencyRepository.save(exchangeCurrency);
        } catch (RuntimeException e) {
            return null;
        }
        ExchangeCurrencyDTO exchangeCurrencyDTO = exchangeCurrencyMapper.fromEntityToDTO(exchangeCurrency);
        exchangeCurrencyDTO.setBaseCurrencyCode(baseCurrencyDTO.getCode());
        exchangeCurrencyDTO.setTargetCurrencyCode(targetCurrencyDTO.getCode());
        return exchangeCurrencyDTO;
    }

    public ExchangeCurrencyDTO updateExchangeCurrency(Long id, BigDecimal countBaseCash) {
        ExchangeCurrency exchangeCurrency = exchangeCurrencyRepository.findById(id).orElse(null);

        if (exchangeCurrency == null) return null;

        exchangeCurrency.setCountBaseCash(countBaseCash);

        try {
            exchangeCurrency = exchangeCurrencyRepository.save(exchangeCurrency);
        } catch (RuntimeException e) {
            return null;
        }

        return exchangeCurrencyMapper.fromEntityToDTO(exchangeCurrency);
    }

    public void deleteExchangeCurrency(Long id) {
        exchangeCurrencyRepository.deleteById(id);
    }
    
}
