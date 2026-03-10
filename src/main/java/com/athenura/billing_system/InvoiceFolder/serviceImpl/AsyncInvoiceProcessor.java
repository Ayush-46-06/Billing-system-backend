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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AsyncInvoiceProcessor {

    private final InvoicePdfService invoicePdfService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final InvoiceRepository invoiceRepository;
@Async
@Transactional
public void processInvoice(Long invoiceId) {

    Invoice invoice = null;

    try {

        System.out.println("STEP 1 : Fetch invoice");

        invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        Client client = invoice.getClient();

        System.out.println("STEP 2 : Convert DTO");

        InvoiceResponseDTO dto = InvoiceMapper.toDTO(invoice);

        System.out.println("STEP 3 : Generate PDF");

        byte[] pdfBytes = invoicePdfService.generateInvoicePdf(dto);

        System.out.println("STEP 4 : Upload Cloudinary");

        String pdfUrl = cloudinaryService.uploadPdf(pdfBytes, invoice.getInvoiceNumber());

        invoice.setPdfUrl(pdfUrl);

        System.out.println("STEP 5 : Send Email");

        emailService.sendInvoiceEmail(
                client.getEmail(),
                pdfBytes,
                invoice.getInvoiceNumber() + ".pdf"
        );

        invoice.setStatus(InvoiceStatus.SENT);

        invoiceRepository.save(invoice);

        System.out.println("ASYNC SUCCESS");

    } catch (Exception e) {

        e.printStackTrace();

        if (invoice != null) {
            invoice.setStatus(InvoiceStatus.FAILED);
            invoiceRepository.save(invoice);
        }
    }
}
}