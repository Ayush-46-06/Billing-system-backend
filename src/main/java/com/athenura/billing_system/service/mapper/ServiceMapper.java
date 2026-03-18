package com.athenura.billing_system.service.mapper;

import com.athenura.billing_system.service.dto.serviceRequestDto;
import com.athenura.billing_system.service.dto.serviceResponseDto;
import com.athenura.billing_system.service.entity.ServiceEntity;
import com.athenura.billing_system.service.entity.ServiceStatus;

public class ServiceMapper {

    // Method to convert ServiceEntity to serviceResponseDto
    public static serviceResponseDto toServiceResponseDto(ServiceEntity serviceEntity){
        serviceResponseDto responseDto = new serviceResponseDto();
        responseDto.setServiceName(serviceEntity.getServiceName());
        responseDto.setDescription(serviceEntity.getDescription());
        responseDto.setBasePrice(serviceEntity.getBasePrice());
        responseDto.setGstPercentage(serviceEntity.getGstPercentage());
        responseDto.setActive(serviceEntity.getActive());
        return responseDto;
    }

    // Method to convert serviceRequestDto to ServiceEntity
    public static ServiceEntity toServiceEntity(serviceRequestDto requestDto){
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setServiceName(requestDto.getServiceName());
        serviceEntity.setDescription(requestDto.getDescription());
        serviceEntity.setBasePrice(requestDto.getBasePrice());
        serviceEntity.setGstPercentage(requestDto.getGstPercentage());
        serviceEntity.setActive(ServiceStatus.ACTIVE); // Defaulting to ACTIVE when creating a new service
        return serviceEntity;
    }
}

