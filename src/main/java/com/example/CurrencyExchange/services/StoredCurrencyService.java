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
        return storedCurrencyRepository.findAllByOrderById()
                .stream()
                .map(storedCurrencyMapper::fromEntityToDTO)
                .toList();
    }

    public StoredCurrencyDTO getStoredCurrency(Long id) {
        return storedCurrencyMapper.fromEntityToDTO(
                storedCurrencyRepository.findById(id).orElse(null)
        );
    }

    public StoredCurrencyDTO getStoredCurrencyByCodeFromCashRegister(Long cashRegId, String code) {
        return storedCurrencyMapper.fromEntityToDTO(
                storedCurrencyRepository.getStoredCurrencyByCode(cashRegId, code).orElse(null)
        );
    }

    public StoredCurrencyDTO addStoredCurrency(Long curId, Long cashRegId, StoredCurrencyDTO storedCurrencyDTO) {
        if (currencyService.getCurrency(curId) == null || registerService.getCashRegister(cashRegId) == null) return null;

        storedCurrencyDTO.setCashRegisterId(cashRegId);
        StoredCurrency storedCurrency = storedCurrencyMapper.fromDTOToEntity(storedCurrencyDTO, curId);
        try {
            storedCurrency = storedCurrencyRepository.save(storedCurrency);
        }
        catch (Exception e){
            return null;
        }
        return storedCurrencyMapper.fromEntityToDTO(storedCurrency);
    }

    public StoredCurrencyDTO updateStoredCurrency(Long id, StoredCurrencyDTO storedCurrencyDTO) {
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

    public StoredCurrencyDTO changeCountStoredCurrency(Long id, BigDecimal count, boolean isAdd) {
        StoredCurrency storedCurrency = storedCurrencyRepository.findById(id).orElse(null);
        if (storedCurrency == null) return null;

        if (isAdd) {
            // добавляем к текущему количеству валюты
            storedCurrency.setCount(
                    storedCurrency.getCount().add(count)
            );
        } else {
            // убавляем от текущего количества валюты
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
    
    public void deleteStoredCurrency(Long id) {
        storedCurrencyRepository.deleteById(id);
    }
}
