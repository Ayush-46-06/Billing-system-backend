package com.athenura.billing_system.InvoiceFolder.serviceImpl;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import com.athenura.billing_system.InvoiceFolder.entity.Invoice;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.InvoiceFolder.mapper.InvoiceMapper;
import com.athenura.billing_system.InvoiceFolder.repository.InvoiceRepository;
import com.athenura.billing_system.client.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncInvoiceProcessor {

    private final InvoicePdfService invoicePdfService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final InvoiceRepository invoiceRepository;

    @Async
    public void processInvoice(Invoice invoice, Client client) {

        try {

            InvoiceResponseDTO dto = InvoiceMapper.toDTO(invoice);

            // 1 PDF generate
            byte[] pdfBytes = invoicePdfService.generateInvoicePdf(dto);

            // 2 upload cloudinary
            String pdfUrl = cloudinaryService.uploadPdf(pdfBytes, invoice.getInvoiceNumber());
            invoice.setPdfUrl(pdfUrl);

            // 3 send email
            emailService.sendInvoiceEmail(
                    client.getEmail(),
                    pdfBytes,
                    invoice.getInvoiceNumber() + ".pdf"
            );

            invoice.setStatus(InvoiceStatus.SENT);

        } catch (Exception e) {

            invoice.setStatus(InvoiceStatus.FAILED);

        }

        invoiceRepository.save(invoice);
    }
}