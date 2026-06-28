package com.athenura.billing_system.InvoiceFolder.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${brevo.api.key:}")
    private String brevoApiKey;

    @Value("${brevo.sender.email:info@billing.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Billing System}")
    private String senderName;

    public void sendInvoiceEmail(String toEmail,
                                 byte[] pdfBytes,
                                 String fileName) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String base64Content = Base64.getEncoder().encodeToString(pdfBytes);

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", senderName, "email", senderEmail),
                "to", List.of(Map.of("email", toEmail)),
                "subject", "Your Invoice",
                "htmlContent", "<p>Please find attached your service invoice.</p>",
                "attachment", List.of(
                        Map.of(
                                "name", fileName,
                                "content", base64Content
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send invoice email via Brevo: " + e.getMessage());
        }
    }
}