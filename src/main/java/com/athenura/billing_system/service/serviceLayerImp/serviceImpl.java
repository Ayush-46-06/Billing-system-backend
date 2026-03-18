package com.athenura.billing_system.service.serviceLayerImp;


import java.util.List;

import org.springframework.stereotype.Service;

import com.athenura.billing_system.service.dto.ServiceUpdateRequestDto;
import com.athenura.billing_system.service.dto.serviceRequestDto;
import com.athenura.billing_system.service.dto.serviceResponseDto;
import com.athenura.billing_system.service.entity.ServiceEntity;
import com.athenura.billing_system.service.mapper.ServiceMapper;
import com.athenura.billing_system.service.repository.ServiceRepository;
import com.athenura.billing_system.service.serviceLayer.ServiceLayerMethod;


@Service
public class serviceImpl implements ServiceLayerMethod {

    ServiceRepository serviceRepository;

    public serviceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public serviceResponseDto createService(serviceRequestDto requestDto) {
        ServiceEntity serviceEntity = ServiceMapper.toServiceEntity(requestDto);
        ServiceEntity savedService = serviceRepository.save(serviceEntity);
        return ServiceMapper.toServiceResponseDto(savedService);
    }

    @Override
    public serviceResponseDto getServiceByName(Long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return ServiceMapper.toServiceResponseDto(serviceEntity);
    }

    @Override
    public serviceResponseDto updateService(Long id, ServiceUpdateRequestDto requestDto) {
        ServiceEntity existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        existingService.setServiceName(requestDto.getServiceName());
        existingService.setDescription(requestDto.getDescription());
        existingService.setBasePrice(requestDto.getBasePrice());
        existingService.setGstPercentage(requestDto.getGstPercentage());
        existingService.setActive(requestDto.getActive());
        ServiceEntity updatedService = serviceRepository.save(existingService);
        return ServiceMapper.toServiceResponseDto(updatedService);
    }

@Override
public List<serviceResponseDto> getAllService() {

    List<ServiceEntity> services = serviceRepository.findAll();

    if (services.isEmpty()) {
        throw new RuntimeException("No services found");
    }

    return services.stream()
            .map(ServiceMapper::toServiceResponseDto)
            .toList();
}

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

}
