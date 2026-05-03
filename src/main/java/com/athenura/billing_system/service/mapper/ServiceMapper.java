package com.athenura.billing_system.service.mapper;

import com.athenura.billing_system.service.dto.serviceRequestDto;
import com.athenura.billing_system.service.dto.serviceResponseDto;
import com.athenura.billing_system.service.entity.ServiceEntity;
import com.athenura.billing_system.service.entity.ServiceStatus;

public class ServiceMapper {


    public static serviceResponseDto toServiceResponseDto(ServiceEntity serviceEntity){
        serviceResponseDto responseDto = new serviceResponseDto();
        responseDto.setId(serviceEntity.getId());
        responseDto.setServiceName(serviceEntity.getServiceName());
        responseDto.setDescription(serviceEntity.getDescription());
        responseDto.setBasePrice(serviceEntity.getBasePrice());
//        responseDto.setGstPercentage(serviceEntity.getGstPercentage());
        responseDto.setActive(serviceEntity.getActive());
        return responseDto;
    }


    public static ServiceEntity toServiceEntity(serviceRequestDto requestDto){
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setServiceName(requestDto.getServiceName());
        serviceEntity.setDescription(requestDto.getDescription());
        serviceEntity.setBasePrice(requestDto.getBasePrice());
//        serviceEntity.setGstPercentage(requestDto.getGstPercentage());
        serviceEntity.setActive(ServiceStatus.ACTIVE);
        return serviceEntity;
    }
}

