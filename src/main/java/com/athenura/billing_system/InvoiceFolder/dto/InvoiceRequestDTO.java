package com.athenura.billing_system.InvoiceFolder.dto;

import java.time.LocalDate;
import java.util.List;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.InvoiceFolder.entity.TaxType;
import com.athenura.billing_system.InvoiceFolder.entity.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDTO {

    private Long clientId;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private TaxType taxType;
    private InvoiceStatus status;
    private List<InvoiceItemRequestDTO> items;
    private Double taxPercentage;
    private PaymentStatus paymentStatus;
}