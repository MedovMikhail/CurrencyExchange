package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CurrencyDTO;
import com.example.CurrencyExchange.entities.Currency;
import com.example.CurrencyExchange.repositories.CurrencyRepository;
import com.example.CurrencyExchange.utils.mapping.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyMapper currencyMapper;
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<CurrencyDTO> getCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(currencyMapper::fromEntityToDTO)
                .toList();
    }

    public CurrencyDTO getCurrency(long id) {
        return currencyMapper.fromEntityToDTO(
                currencyRepository.findById(id).orElse(null)
        );
    }

    public CurrencyDTO getCurrency(String code) {
        return currencyMapper.fromEntityToDTO(
                currencyRepository.findByCode(code.toUpperCase())
        );
    }

    public CurrencyDTO addCurrency(CurrencyDTO currencyDTO) {
        currencyDTO.setCode(currencyDTO.getCode().toUpperCase());
        try {
            Currency currency = currencyRepository.save(
                    currencyMapper.fromDTOToEntity(currencyDTO)
            );
            return currencyMapper.fromEntityToDTO(currency);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    public CurrencyDTO updateCurrency(long id, CurrencyDTO currencyDTO) {
        if (currencyRepository.findById(id).isEmpty()) return null;
        currencyDTO.setId(id);
        currencyDTO.setCode(currencyDTO.getCode().toUpperCase());
        Currency currency = currencyMapper.fromDTOToEntity(currencyDTO);
        try {
            currency = currencyRepository.save(currency);
            return currencyMapper.fromEntityToDTO(currency);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    public void deleteCurrency(long id){
        currencyRepository.deleteById(id);
    }

    public void deleteCurrency(String code){
        currencyRepository.deleteByCode(code.toUpperCase());
    }
}
