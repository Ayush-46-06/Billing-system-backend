package com.athenura.billing_system.service.controller;

import com.athenura.billing_system.service.entity.ServiceEntity;
import com.athenura.billing_system.service.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public List<ServiceEntity> getAllServices(){
        return serviceRepository.findAll();
    }

    
}
