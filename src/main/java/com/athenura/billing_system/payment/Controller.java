package com.athenura.billing_system.payment;

import com.athenura.billing_system.InvoiceFolder.entity.Invoice;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.InvoiceFolder.entity.PaymentStatus;
import com.athenura.billing_system.InvoiceFolder.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class Controller {

    private final InvoiceRepository invoiceRepository;

    @GetMapping("/dashboard-stats")
    public Map<String, Object> getDashboardStats() {

        Map<String, Object> response = new HashMap<>();


        BigDecimal settledAmt = invoiceRepository.sumByPaymentStatus(PaymentStatus.PAID);
        BigDecimal overdueAmt = invoiceRepository.sumByPaymentStatus(PaymentStatus.OVERDUE);


        long totalInvoices = invoiceRepository.count();
        long paidCount = invoiceRepository.countByPaymentStatus(PaymentStatus.PAID);
        long overdueCount = invoiceRepository.countByPaymentStatus(PaymentStatus.OVERDUE);


        settledAmt = (settledAmt != null) ? settledAmt : BigDecimal.ZERO;
        overdueAmt = (overdueAmt != null) ? overdueAmt : BigDecimal.ZERO;


        double successPercent = (totalInvoices > 0) ? ((double) paidCount / totalInvoices) * 100 : 0.0;

        // --- STATS SECTION ---
        Map<String, Object> stats = new HashMap<>();


        stats.put("settled", Map.of(
                "amount", "₹" + String.format("%,.2f", settledAmt),
                "change", "+0%"
        ));


        stats.put("success", Map.of(
                "rate", String.format("%.2f%%", successPercent),
                "latency", "0.5s"
        ));


        stats.put("overdue", Map.of(
                "amount", "₹" + String.format("%,.2f", overdueAmt),
                "count", String.valueOf(overdueCount)
        ));

        response.put("stats", stats);


        List<Invoice> latestInvoices = invoiceRepository.findTop5ByOrderByInvoiceDateDesc();
        List<Map<String, String>> stream = new ArrayList<>();

        for (Invoice inv : latestInvoices) {
            Map<String, String> item = new HashMap<>();

            String clientName = (inv.getClient() != null) ? inv.getClient().getName() : "Unknown Client";

            item.put("name", clientName);
            item.put("ref", inv.getInvoiceNumber() != null ? inv.getInvoiceNumber() : "#INV-000");
            item.put("method", "Standard");
            item.put("amt", "₹" + String.format("%,.2f", inv.getGrandTotal()));
            item.put("status", inv.getStatus() != null ? inv.getStatus().name() : "PENDING");
            item.put("time", inv.getInvoiceDate() != null ? inv.getInvoiceDate().toString() : "N/A");

            stream.add(item);
        }

        response.put("stream", stream);

        response.put("systemHealth", List.of(
                Map.of("name", "M", "val", 50),
                Map.of("name", "T", "val", 80),
                Map.of("name", "W", "val", 65),
                Map.of("name", "T", "val", 90),
                Map.of("name", "F", "val", 75)
        ));

        response.put("forecast", Map.of(
                "amount", "₹" + String.format("%,.0f", overdueAmt),
                "clients", String.valueOf(overdueCount)
        ));

        response.put("watchlist", new ArrayList<>());

        return response;
    }

    @PostMapping("/pay/{invoiceId}")
    public String markAsPaid(@PathVariable Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoiceRepository.save(invoice);

        return "Payment successful";
    }

    @PostMapping("/unpaid/{invoiceId}")
    public String markAsUnpaid(@PathVariable Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setPaymentStatus(PaymentStatus.UNPAID);
        invoiceRepository.save(invoice);

        return "Updated";
    }
}