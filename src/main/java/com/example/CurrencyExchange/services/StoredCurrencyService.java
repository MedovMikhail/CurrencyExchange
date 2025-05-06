package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.StoredCurrencyDTO;
import com.example.CurrencyExchange.entities.StoredCurrency;
import com.example.CurrencyExchange.repositories.StoredCurrencyRepository;
import com.example.CurrencyExchange.utils.mapping.StoredCurrencyMapper;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StoredCurrencyService {

    @Autowired
    private StoredCurrencyRepository storedCurrencyRepository;
    @Autowired
    private StoredCurrencyMapper storedCurrencyMapper;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CashRegisterService registerService;

    public List<StoredCurrencyDTO> getStoredCurrencies() {
        return storedCurrencyRepository.findAll()
                .stream()
                .map(storedCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public StoredCurrencyDTO getStoredCurrency(long id) {
        return storedCurrencyMapper.fromEntityToDTO(
                storedCurrencyRepository.findById(id).orElse(null)
        );
    }

    public StoredCurrencyDTO createStoredCurrency(long curId, long cashRegId, StoredCurrencyDTO storedCurrencyDTO) {
        if (currencyService.getCurrency(curId) == null || registerService.getCashRegister(cashRegId) == null) return null;

        storedCurrencyDTO.setCurrencyId(curId);
        storedCurrencyDTO.setCashRegisterId(cashRegId);
        StoredCurrency storedCurrency = storedCurrencyMapper.fromDTOToEntity(storedCurrencyDTO);
        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
        }
        catch (Exception e){
            return null;
        }
        return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
    }

    public StoredCurrencyDTO updateStoredCurrency(long id, StoredCurrencyDTO storedCurrencyDTO) {
        StoredCurrency storedCurrency = storedCurrencyRepository.findById(id).orElse(null);
        if (storedCurrency == null) return null;

        storedCurrency.setCount(storedCurrencyDTO.getCount());
        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
            return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    public StoredCurrencyDTO changeCountStoredCurrency(long id, BigDecimal count, boolean isAdd) {
        StoredCurrency storedCurrency = storedCurrencyRepository.findById(id).orElse(null);
        if (storedCurrency == null) return null;

        if (isAdd) {
            storedCurrency.setCount(
                    storedCurrency.getCount().add(count)
            );
        } else {
            storedCurrency.setCount(
                    storedCurrency.getCount().subtract(count)
            );
        }

        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
            return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
        } catch (NonUniqueResultException e) {
            return null;
        }
    }
    
    public void deleteStoredCurrency(long id) {
        storedCurrencyRepository.deleteById(id);
    }
}
