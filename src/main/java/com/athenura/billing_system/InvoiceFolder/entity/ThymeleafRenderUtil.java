package com.athenura.billing_system.InvoiceFolder.entity;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;

@Component
@RequiredArgsConstructor
public class ThymeleafRenderUtil {

    private final TemplateEngine templateEngine;

    public byte[] renderTemplate(String templateName, Context context) {
        try {
            String htmlContent = templateEngine.process(templateName, context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF generation failed: " + e.getMessage());
        }
    }
}