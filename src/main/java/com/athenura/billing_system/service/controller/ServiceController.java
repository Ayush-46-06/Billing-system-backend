package com.athenura.billing_system.service.controller;

import com.athenura.billing_system.service.dto.ServiceUpdateRequestDto;
import com.athenura.billing_system.service.dto.serviceRequestDto;
import com.athenura.billing_system.service.dto.serviceResponseDto;
import com.athenura.billing_system.service.serviceLayer.ServiceLayerMethod;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/services")
public class ServiceController {

    private final ServiceLayerMethod serviceLayerMethod;

    @PostMapping
    public ResponseEntity<serviceResponseDto> createService(@RequestBody serviceRequestDto requestDto) {
        serviceResponseDto serviceResponseDto = serviceLayerMethod.createService(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<serviceResponseDto>> getAllServices() {
        List<serviceResponseDto> services = serviceLayerMethod.getAllService();
        return ResponseEntity.ok().body(services);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<serviceResponseDto> updateService(@PathVariable Long id, @RequestBody ServiceUpdateRequestDto requestDto) {
        serviceResponseDto serviceResponseDto = serviceLayerMethod.updateService(id, requestDto);
        return ResponseEntity.ok().body(serviceResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceLayerMethod.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<serviceResponseDto> getServiceById(@PathVariable Long id) {
        serviceResponseDto serviceResponseDto = serviceLayerMethod.getServiceByName(id);
        return ResponseEntity.ok().body(serviceResponseDto);
    }
}
