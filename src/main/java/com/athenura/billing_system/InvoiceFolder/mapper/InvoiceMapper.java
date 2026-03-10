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

        Invoice invoice = new Invoice();
        invoice.setClient(client);
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setTaxType(dto.getTaxType());
        invoice.setStatus(InvoiceStatus.DRAFT);

        return invoice;
    }

    public static InvoiceResponseDTO toDTO(Invoice invoice) {

        return new InvoiceResponseDTO(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getClient().getId(),
                invoice.getClient().getName(),
                invoice.getClient().getEmail(),
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
                        .collect(Collectors.toList())
        );
    }
}