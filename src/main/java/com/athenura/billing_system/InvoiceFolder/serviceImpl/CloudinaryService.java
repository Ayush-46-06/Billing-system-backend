package com.athenura.billing_system.InvoiceFolder.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadPdf(byte[] pdfBytes, String invoiceNumber) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(
                pdfBytes,
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "public_id", "invoices/" + invoiceNumber
                )
        );

        return uploadResult.get("secure_url").toString();
    }


    public byte[] downloadPdf(String pdfUrl) {
        try {
            URL url = new URL(pdfUrl);
            InputStream inputStream = url.openStream();
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to download PDF from Cloudinary", e);
        }
    }
}