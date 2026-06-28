package com.athenura.billing_system.InvoiceFolder.controller;

import com.athenura.billing_system.InvoiceFolder.dto.InvoiceRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import com.athenura.billing_system.InvoiceFolder.services.InvoiceService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/create")
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO dto) {
        return new ResponseEntity<>(invoiceService.createInvoice(dto), HttpStatus.CREATED);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InvoiceResponseDTO> updateInvoice(@PathVariable Long id, @RequestBody InvoiceRequestDTO dto) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, dto));
    }
}