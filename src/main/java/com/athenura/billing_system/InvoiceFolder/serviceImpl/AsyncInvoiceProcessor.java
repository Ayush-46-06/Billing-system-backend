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

        System.out.println(" ASYNC STARTED for invoice: " + invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            System.out.println("Invoice not found");
            return;
        }

        try {
            System.out.println("STEP 1: Set PENDING");
            invoice.setStatus(InvoiceStatus.PENDING);
            invoiceRepository.save(invoice);

            System.out.println("STEP 2: Convert DTO");
            InvoiceResponseDTO dto = InvoiceMapper.toDTO(invoice);

            System.out.println("STEP 3: Generate PDF");
            byte[] pdfBytes = invoicePdfService.generateInvoicePdf(dto);

            System.out.println("STEP 4: Upload to Cloudinary");
            String pdfUrl = cloudinaryService.uploadPdf(pdfBytes, invoice.getInvoiceNumber());

            if (pdfUrl == null || pdfUrl.isEmpty()) {
                throw new RuntimeException("Cloudinary returned null URL");
            }

            System.out.println("STEP 5: Save PDF URL");
            invoice.setPdfUrl(pdfUrl);

            System.out.println("STEP 6: Mark READY");
            invoice.setStatus(InvoiceStatus.DRAFT);
            invoiceRepository.save(invoice);

            System.out.println("PDF SUCCESS for invoice: " + invoice.getInvoiceNumber());

        } catch (Exception e) {

            System.out.println("PDF FAILED for invoice: " + invoiceId);
            e.printStackTrace();

            invoice.setStatus(InvoiceStatus.FAILED);
            invoiceRepository.save(invoice);
        }
    }}