package com.athenura.billing_system.InvoiceFolder.mapper;

import java.math.BigDecimal;

import com.athenura.billing_system.InvoiceFolder.dto.InvoiceItemResponseDTO;
import com.athenura.billing_system.InvoiceFolder.entity.Invoice;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceItem;
import com.athenura.billing_system.service.entity.ServiceEntity;

public class InvoiceItemMapper {

    private InvoiceItemMapper() {}

    public static InvoiceItem toEntity(
            Invoice invoice,
            ServiceEntity service,
            String description,
            BigDecimal rate,
            BigDecimal lineTotal) {

        InvoiceItem item = new InvoiceItem();

        item.setInvoice(invoice);
        item.setService(service);
        item.setDescription(description);
        item.setRate(rate);
        item.setLineTotal(lineTotal);

        return item;
    }

    public static InvoiceItemResponseDTO toDTO(InvoiceItem item) {

        return new InvoiceItemResponseDTO(
                item.getId(),
                item.getService().getId(),
                item.getService().getServiceName(),
                item.getDescription(),
                item.getRate(),
                item.getLineTotal()
        );
    }
}