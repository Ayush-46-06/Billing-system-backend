package com.athenura.billing_system.InvoiceFolder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.athenura.billing_system.InvoiceFolder.entity.Invoice;
import com.athenura.billing_system.InvoiceFolder.entity.InvoiceStatus;
import com.athenura.billing_system.InvoiceFolder.entity.PaymentStatus;
import java.math.BigDecimal;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCreatedBy(String username);


    List<Invoice> findByCreatedByAndCreatedByRole(String name, String role);

    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.status = :status")
    BigDecimal sumInvoicesByStatus(@Param("status") InvoiceStatus status);

    long countByStatus(InvoiceStatus status);

    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.paymentStatus = :status")
    BigDecimal sumByPaymentStatus(@Param("status") PaymentStatus status);

    long countByPaymentStatus(PaymentStatus status);

    List<Invoice> findTop5ByOrderByInvoiceDateDesc();

    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.status = :status AND (i.createdBy = :name OR i.createdBy = :email)")
    BigDecimal sumInvoicesByStatusAndUser(@Param("status") InvoiceStatus status, @Param("name") String name, @Param("email") String email);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status AND (i.createdBy = :name OR i.createdBy = :email)")
    long countByStatusAndUser(@Param("status") InvoiceStatus status, @Param("name") String name, @Param("email") String email);

    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.paymentStatus = :status AND (i.createdBy = :name OR i.createdBy = :email)")
    BigDecimal sumByPaymentStatusAndUser(@Param("status") PaymentStatus status, @Param("name") String name, @Param("email") String email);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.paymentStatus = :status AND (i.createdBy = :name OR i.createdBy = :email)")
    long countByPaymentStatusAndUser(@Param("status") PaymentStatus status, @Param("name") String name, @Param("email") String email);

    @Query("SELECT i FROM Invoice i WHERE (i.createdBy = :name OR i.createdBy = :email) ORDER BY i.invoiceDate DESC")
    List<Invoice> findRecentInvoicesByUser(@Param("name") String name, @Param("email") String email);

    @Query("""
        SELECT i FROM Invoice i
        JOIN FETCH i.client
        JOIN FETCH i.items items
        JOIN FETCH items.service
        WHERE i.id = :id
    """)
    Invoice findByIdWithDetails(@Param("id") Long id);
}