package com.athenura.billing_system.InvoiceFolder.serviceImpl;

import com.athenura.billing_system.InvoiceFolder.dto.InvoiceResponseDTO;
import com.athenura.billing_system.InvoiceFolder.entity.ThymeleafRenderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.thymeleaf.context.Context;

import java.io.InputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class InvoicePdfService {

    private final ThymeleafRenderUtil thymeleafRenderUtil;
    
    public byte[] generateInvoicePdf(InvoiceResponseDTO invoiceDTO) {
        Context context = new Context();
        context.setVariable("invoice", invoiceDTO);
        
        try {
            ClassPathResource resource = new ClassPathResource("static/images/Athenura.png");
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] imageBytes = StreamUtils.copyToByteArray(inputStream);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                context.setVariable("logoBase64", "data:image/png;base64," + base64Image);
            }
        } catch (Exception e) {
            System.err.println("Failed to load logo: " + e.getMessage());
        }

        return thymeleafRenderUtil.renderTemplate("invoice", context);
    }
}