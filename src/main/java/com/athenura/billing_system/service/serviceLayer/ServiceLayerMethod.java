package com.athenura.billing_system.service.serviceLayer;

import java.util.List;

import com.athenura.billing_system.service.dto.ServiceUpdateRequestDto;
import com.athenura.billing_system.service.dto.serviceRequestDto;
import com.athenura.billing_system.service.dto.serviceResponseDto;

public interface ServiceLayerMethod {

    public serviceResponseDto createService(serviceRequestDto requestDto);
    public serviceResponseDto getServiceByName(Long id);
    public List<serviceResponseDto> getAllService();
    public serviceResponseDto updateService(Long id, ServiceUpdateRequestDto requestDto);
    public void deleteService(Long id);
}
