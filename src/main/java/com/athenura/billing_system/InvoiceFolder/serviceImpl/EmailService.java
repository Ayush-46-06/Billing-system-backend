package com.athenura.billing_system.InvoiceFolder.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendInvoiceEmail(String toEmail,
                                 byte[] pdfBytes,
                                 String fileName)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Invoice");
        helper.setText("Please find attached your service invoice.");

        helper.addAttachment(
                fileName,
                new ByteArrayResource(pdfBytes)
        );

        mailSender.send(message);
    }
}