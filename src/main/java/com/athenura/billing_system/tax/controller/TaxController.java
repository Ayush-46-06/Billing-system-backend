package com.athenura.billing_system.tax.controller;

import com.athenura.billing_system.tax.entity.Tax;
import com.athenura.billing_system.tax.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @GetMapping
    public List<Tax> getAll() {
        return taxService.getAllTaxes();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Tax create(@RequestBody Tax tax) {
        return taxService.createTax(tax);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taxService.deleteTax(id);
    }
}