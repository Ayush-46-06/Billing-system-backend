package com.athenura.billing_system.InvoiceFolder.serviceImpl;

import java.io.IOException;
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
}