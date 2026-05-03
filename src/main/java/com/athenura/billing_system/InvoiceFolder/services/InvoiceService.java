package com.athenura.billing_system.InvoiceFolder.services;

import java.util.List;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import jakarta.mail.MessagingException;

public interface InvoiceService {
    InvoiceResponseDTO createInvoice(InvoiceRequestDTO dto);
    InvoiceResponseDTO getInvoiceById(Long id);
    List<InvoiceResponseDTO> getAllInvoices();
    InvoiceResponseDTO updateInvoice(Long id, InvoiceRequestDTO dto) throws MessagingException;
    void deleteInvoice(Long id);
}