package com.athenura.billing_system.InvoiceFolder.serviceImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceItemRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import com.athenura.billing_system.InvoiceFolder.entity.Invoice;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceItem;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.InvoiceFolder.entity.TaxType;
import com.athenura.billing_system.InvoiceFolder.mapper.InvoiceItemMapper;
import com.athenura.billing_system.InvoiceFolder.mapper.InvoiceMapper;
import com.athenura.billing_system.InvoiceFolder.repository.InvoiceRepository;
import com.athenura.billing_system.InvoiceFolder.services.InvoiceService;
import com.athenura.billing_system.client.entity.Client;
import com.athenura.billing_system.client.repository.ClientRepository;
import com.athenura.billing_system.service.entity.ServiceEntity;
import com.athenura.billing_system.service.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;
    private final AsyncInvoiceProcessor asyncProcessor;

    private static final BigDecimal GST_PERCENT = new BigDecimal("18");

    @Override
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Invoice invoice = InvoiceMapper.toEntity(dto, client);
        invoice.setInvoiceNumber(generateInvoiceNumber());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (InvoiceItemRequestDTO itemDTO : dto.getItems()) {

            ServiceEntity service = serviceRepository.findById(itemDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            BigDecimal rate = BigDecimal.valueOf(service.getBasePrice());

            BigDecimal lineTotal = rate;

            subtotal = subtotal.add(lineTotal);

            InvoiceItem item = InvoiceItemMapper.toEntity(
                    invoice,
                    service,
                    service.getServiceName(),
                    rate,
                    lineTotal
            );

            invoice.getItems().add(item);
        }

        invoice.setSubtotal(subtotal);

        TaxType taxType = dto.getTaxType();

        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal cgst = BigDecimal.ZERO;
        BigDecimal sgst = BigDecimal.ZERO;
        BigDecimal igst = BigDecimal.ZERO;

        if (taxType != null && taxType != TaxType.NONE) {

            taxTotal = subtotal
                    .multiply(GST_PERCENT)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (taxType == TaxType.CGST_SGST) {

                BigDecimal half = taxTotal
                        .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

                cgst = half;
                sgst = half;

            } else if (taxType == TaxType.IGST) {

                igst = taxTotal;
            }
        }

        BigDecimal grandTotal = subtotal.add(taxTotal);

        invoice.setTaxPercent(GST_PERCENT);
        invoice.setTaxTotal(taxTotal);
        invoice.setCgst(cgst);
        invoice.setSgst(sgst);
        invoice.setIgst(igst);
        invoice.setGrandTotal(grandTotal);
        invoice.setTaxType(taxType);

        invoice.setStatus(InvoiceStatus.PENDING);

Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);
asyncProcessor.processInvoice(savedInvoice.getId());

        return InvoiceMapper.toDTO(savedInvoice);
    }

    private String generateInvoiceNumber() {

        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 5)
                .toUpperCase();

        return "INV-" + datePart + "-" + randomPart;
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return InvoiceMapper.toDTO(invoice);
    }
}