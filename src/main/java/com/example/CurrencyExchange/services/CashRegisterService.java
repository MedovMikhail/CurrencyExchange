package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.CashRegisterDTO;
import com.example.CurrencyExchange.entities.CashRegister;
import com.example.CurrencyExchange.repositories.CashRegisterRepository;
import com.example.CurrencyExchange.utils.mapping.CashRegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashRegisterService {

    @Autowired
    private CashRegisterMapper cashRegisterMapper;
    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    public List<CashRegisterDTO> getCashRegisters() {
        return cashRegisterRepository.findAll(Sort.by("id"))
                .stream()
                .map(cashRegisterMapper::fromEntityToDTO)
                .toList();
    }

    public List<CashRegisterDTO> getCashRegisters(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return cashRegisterRepository.findAll(pageable)
                .stream()
                .map(cashRegisterMapper::fromEntityToDTO)
                .toList();
    }

    public CashRegisterDTO getCashRegister(Long id) {
        return cashRegisterMapper.fromEntityToDTO(
                cashRegisterRepository.findById(id).orElse(null)
        );
    }

    // добавить новую кассу
    public CashRegisterDTO addCashRegister(CashRegisterDTO cashRegisterDto) {
        CashRegister cashRegister = cashRegisterMapper.fromDTOToEntity(cashRegisterDto);
        try{
            // пытаемся сохранить
            cashRegister = cashRegisterRepository.save(cashRegister);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return cashRegisterMapper.fromEntityToDTO(cashRegister);
    }

    public CashRegisterDTO updateCashRegister(Long id, CashRegisterDTO cashRegisterDto) {
        // проверяем существует ли касса с таким id
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

    public void deleteCashRegister(Long id) {
        cashRegisterRepository.deleteById(id);
    }
}
