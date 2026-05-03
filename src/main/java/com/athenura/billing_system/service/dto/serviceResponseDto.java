package com.athenura.billing_system.service.dto;

import com.athenura.billing_system.service.entity.ServiceStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class serviceResponseDto {

        private Long id;
        String serviceName;
        String description;
        Double basePrice;
        Double gstPercentage;
        ServiceStatus active;
}
