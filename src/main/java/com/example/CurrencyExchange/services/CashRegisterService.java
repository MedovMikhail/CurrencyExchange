package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CashRegisterDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import com.example.CurrencyExchange.repositories.CashRegisterRepository;
import com.example.CurrencyExchange.utils.mapping.CashRegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashRegisterService {

    @Autowired
    private CashRegisterMapper cashRegisterMapper;
    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    public List<CashRegisterDTO> getCashRegisters() {
        return cashRegisterRepository.findAll().stream()
                .map(cashRegisterMapper::fromEntityToDTO)
                .toList();
    }

    public CashRegisterDTO getCashRegister(long id) {
        return cashRegisterMapper.fromEntityToDTO(cashRegisterRepository.findById(id).orElse(null));
    }

    public CashRegisterDTO addCashRegister(CashRegisterDTO cashRegisterDto) {
        CashRegister cashRegister = cashRegisterMapper.fromDTOToEntity(cashRegisterDto);
        try{
            cashRegister = cashRegisterRepository.save(cashRegister);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return cashRegisterMapper.fromEntityToDTO(cashRegister);
    }

    public CashRegisterDTO updateCashRegister(long id, CashRegisterDTO cashRegisterDto) {
        if (cashRegisterRepository.findById(id).isEmpty()) return null;
        cashRegisterDto.setId(id);
        CashRegister cashRegister = cashRegisterMapper.fromDTOToEntity(cashRegisterDto);
        try {
            cashRegister = cashRegisterRepository.save(cashRegister);
        } catch(DataIntegrityViolationException e) {
            return null;
        }
        return cashRegisterMapper.fromEntityToDTO(cashRegister);

    }

    public void deleteCashRegister(long id) {
        cashRegisterRepository.deleteById(id);
    }
}
