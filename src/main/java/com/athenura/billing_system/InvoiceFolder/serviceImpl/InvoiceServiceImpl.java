package com.athenura.billing_system.InvoiceFolder.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.athenura.billing_system.InvoiceFolder.entity.*;
import com.athenura.billing_system.user.User;
import com.athenura.billing_system.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceItemRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceRequestDTO;
import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import com.athenura.billing_system.InvoiceFolder.mapper.InvoiceItemMapper;
import com.athenura.billing_system.InvoiceFolder.mapper.InvoiceMapper;
import com.athenura.billing_system.InvoiceFolder.repository.InvoiceRepository;
import com.athenura.billing_system.InvoiceFolder.services.InvoiceService;
import com.athenura.billing_system.client.entity.Client;
import com.athenura.billing_system.client.repository.ClientRepository;
import com.athenura.billing_system.service.entity.ServiceEntity;
import com.athenura.billing_system.service.repository.ServiceRepository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

        private final InvoiceRepository invoiceRepository;
        private final ClientRepository clientRepository;
        private final ServiceRepository serviceRepository;
        private final AsyncInvoiceProcessor asyncProcessor;
        private final EmailService emailService;
        private final CloudinaryService cloudinaryService;
        private final UserRepository userRepository;

        @Override
        public InvoiceResponseDTO createInvoice(InvoiceRequestDTO dto) {
                Client client = clientRepository.findById(dto.getClientId())
                        .orElseThrow(() -> new RuntimeException("Client not found"));

                Invoice invoice = InvoiceMapper.toEntity(dto, client);
                invoice.setClientName(client.getName());
                invoice.setInvoiceNumber(generateInvoiceNumber());

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                        String currentEmail = auth.getName();
                        Optional<User> userOpt = userRepository.findByEmail(currentEmail);
                        if (userOpt.isPresent()) {
                                invoice.setCreatedBy(userOpt.get().getName());
                        } else {
                                invoice.setCreatedBy(currentEmail);
                        }
                        String role = auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .findFirst()
                                .orElse("ROLE_MANAGER");
                        invoice.setCreatedByRole(role);
                }

                invoice.setStatus(InvoiceStatus.PENDING);


                if (dto.getPaymentStatus() != null) {
                        invoice.setPaymentStatus(dto.getPaymentStatus());
                } else {
                        invoice.setPaymentStatus(PaymentStatus.UNPAID);
                }

                calculateAndSetInvoiceDetails(invoice, dto);
                Invoice savedInvoice = invoiceRepository.save(invoice);

                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                                asyncProcessor.processInvoice(savedInvoice.getId());
                        }
                });

                return convertToDetailedDTO(savedInvoice);
        }

        @Override
        public InvoiceResponseDTO updateInvoice(Long id, InvoiceRequestDTO dto) {
                Invoice invoice = Optional.ofNullable(invoiceRepository.findByIdWithDetails(id))
                        .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

                if (dto.getClientId() != null) {
                        Client client = clientRepository.findById(dto.getClientId())
                                .orElseThrow(() -> new RuntimeException("Client not found"));
                        invoice.setClient(client);
                }

                if (dto.getInvoiceDate() != null) invoice.setInvoiceDate(dto.getInvoiceDate());
                if (dto.getDueDate() != null) invoice.setDueDate(dto.getDueDate());
                if (dto.getStatus() != null) invoice.setStatus(dto.getStatus());
                if (dto.getTaxType() != null) invoice.setTaxType(dto.getTaxType());

                // Update payment status if provided
                if (dto.getPaymentStatus() != null) {
                        invoice.setPaymentStatus(dto.getPaymentStatus());
                }

                if (dto.getItems() != null && !dto.getItems().isEmpty()) {
                        invoice.getItems().clear();
                        calculateAndSetInvoiceDetails(invoice, dto);
                }

                Invoice updated = invoiceRepository.save(invoice);

                if (dto.getStatus() == InvoiceStatus.SENT) {
                        if (updated.getPdfUrl() == null) {
                                throw new RuntimeException("PDF not generated yet");
                        }
                        byte[] pdfBytes = cloudinaryService.downloadPdf(updated.getPdfUrl());
                        if (updated.getClient() != null) {
                                emailService.sendInvoiceEmail(
                                        updated.getClient().getEmail(),
                                        pdfBytes,
                                        updated.getInvoiceNumber() + ".pdf"
                                );
                        }
                        updated.setStatus(InvoiceStatus.SENT);
                        invoiceRepository.save(updated);
                }
                return convertToDetailedDTO(updated);
        }

        @Override
        public List<InvoiceResponseDTO> getAllInvoices() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String currentEmail = auth.getName();
                boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                List<Invoice> invoices;
                if (isAdmin) {
                        invoices = invoiceRepository.findAll();
                } else {
                        Optional<User> userOpt = userRepository.findByEmail(currentEmail);
                        if (userOpt.isPresent()) {
                                invoices = invoiceRepository.findByCreatedByAndCreatedByRole(userOpt.get().getName(), "ROLE_MANAGER");
                        } else {
                                invoices = invoiceRepository.findByCreatedBy(currentEmail);
                        }
                }
                return invoices.stream().map(this::convertToDetailedDTO).collect(Collectors.toList());
        }

        private InvoiceResponseDTO convertToDetailedDTO(Invoice invoice) {
                InvoiceResponseDTO dto = InvoiceMapper.toDTO(invoice);
                dto.setCreatedBy(invoice.getCreatedBy());
                dto.setCreatedByRole(invoice.getCreatedByRole());
                return dto;
        }

        @Override
        public InvoiceResponseDTO getInvoiceById(Long id) {
                Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
                return convertToDetailedDTO(invoice);
        }

        @Override
        public void deleteInvoice(Long id) {
                invoiceRepository.deleteById(id);
        }

        private void calculateAndSetInvoiceDetails(Invoice invoice, InvoiceRequestDTO dto) {
                BigDecimal subtotal = BigDecimal.ZERO;
                if (invoice.getItems() == null) invoice.setItems(new ArrayList<>());
                for (InvoiceItemRequestDTO itemDTO : dto.getItems()) {
                        ServiceEntity service = serviceRepository.findById(itemDTO.getServiceId())
                                .orElseThrow(() -> new RuntimeException("Service not found"));
                        BigDecimal rate = BigDecimal.valueOf(service.getBasePrice());
                        subtotal = subtotal.add(rate);
                        invoice.getItems().add(InvoiceItemMapper.toEntity(invoice, service, service.getServiceName(), rate, rate));
                }
                invoice.setSubtotal(subtotal);
                BigDecimal taxPercent = dto.getTaxPercentage() != null ? BigDecimal.valueOf(dto.getTaxPercentage()) : BigDecimal.ZERO;
                invoice.setTaxPercent(taxPercent);
                BigDecimal totalTax = subtotal.multiply(taxPercent).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                BigDecimal halfTax = totalTax.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
                invoice.setCgst(halfTax);
                invoice.setSgst(halfTax);
                invoice.setIgst(BigDecimal.ZERO);
                invoice.setTaxTotal(totalTax);
                invoice.setGrandTotal(subtotal.add(totalTax));
        }

        private String generateInvoiceNumber() {
                return "INV-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                        "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        }
}