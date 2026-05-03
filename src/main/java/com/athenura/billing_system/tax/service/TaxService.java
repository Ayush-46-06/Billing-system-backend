package com.athenura.billing_system.tax.service;

import com.athenura.billing_system.tax.entity.Tax;
import com.athenura.billing_system.tax.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxRepository taxRepository;

    public List<Tax> getAllTaxes() {
        return taxRepository.findAll();
    }

    public Tax createTax(Tax tax) {
        return taxRepository.save(tax);
    }

    public void deleteTax(Long id) {
        taxRepository.deleteById(id);
    }
}