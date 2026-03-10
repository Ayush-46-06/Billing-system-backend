package com.athenura.billing_system.InvoiceFolder.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.InvoiceFolder.entity.TaxType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceResponseDTO {

    private Long id;
    private String invoiceNumber;

    private Long clientId;
    private String clientName;
    private String clientEmail;

    private LocalDate invoiceDate;
    private LocalDate dueDate;

    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal grandTotal;

    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;

    private TaxType taxType;

    private InvoiceStatus status;

    private String pdfUrl;

    private List<InvoiceItemResponseDTO> items;
}