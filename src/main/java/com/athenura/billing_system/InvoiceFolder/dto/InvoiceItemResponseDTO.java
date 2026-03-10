package com.athenura.billing_system.InvoiceFolder.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceItemResponseDTO {

    private Long id;

    private Long serviceId;
    private String serviceName;

    private String description;
    private BigDecimal rate;

    private BigDecimal lineTotal;
}