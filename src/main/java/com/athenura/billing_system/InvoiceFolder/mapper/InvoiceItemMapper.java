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

        Long serviceId = null;
        String serviceName = null;

        if (item.getService() != null) {
            serviceId = item.getService().getId();
            serviceName = item.getService().getServiceName();
        } else {
            serviceName = item.getDescription();
        }

        return new InvoiceItemResponseDTO(
                item.getId(),
                serviceId,
                serviceName,
                item.getDescription(),
                item.getRate(),
                item.getLineTotal()
        );
    }
}