package com.athenura.billing_system.InvoiceFolder.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.athenura.billing_system.InvoiceFolder.dto.InvoiceRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import com.athenura.billing_system.InvoiceFolder.entity.Invoice;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.client.entity.Client;

public class InvoiceMapper {

    private InvoiceMapper() {}

    public static Invoice toEntity(InvoiceRequestDTO dto, Client client) {
        if (dto == null) return null;

        Invoice invoice = new Invoice();
        invoice.setClient(client);
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setDueDate(dto.getDueDate());


        if (dto.getPaymentStatus() != null) {
            invoice.setPaymentStatus(dto.getPaymentStatus());
        }

        if (dto.getTaxType() != null) {
            invoice.setTaxType(dto.getTaxType());
        } else {
            invoice.setTaxType(com.athenura.billing_system.InvoiceFolder.entity.TaxType.NONE);
        }

        // status
        if (dto.getStatus() != null) {
            invoice.setStatus(dto.getStatus());
        } else {
            invoice.setStatus(InvoiceStatus.PENDING);
        }

        return invoice;
    }

    public static InvoiceResponseDTO toDTO(Invoice invoice) {
        if (invoice == null) return null;

        return new InvoiceResponseDTO(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getClient() != null ? invoice.getClient().getId() : null,
                invoice.getClient() != null
                        ? invoice.getClient().getName()
                        : invoice.getClientName(),
                invoice.getClient() != null ? invoice.getClient().getEmail() : null,
                invoice.getInvoiceDate(),
                invoice.getDueDate(),
                invoice.getSubtotal(),
                invoice.getTaxTotal(),
                invoice.getGrandTotal(),
                invoice.getCgst(),
                invoice.getSgst(),
                invoice.getIgst(),
                invoice.getTaxType(),
                invoice.getStatus(),
                invoice.getPdfUrl(),
                invoice.getItems() == null ? Collections.emptyList()
                        : invoice.getItems()
                          .stream()
                          .map(InvoiceItemMapper::toDTO)
                          .collect(Collectors.toList()),
                invoice.getPaymentStatus(),
                invoice.getCreatedBy(),
                invoice.getCreatedByRole()
        );
    }
}