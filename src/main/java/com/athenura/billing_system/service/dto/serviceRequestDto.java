package com.athenura.billing_system.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class serviceRequestDto {

    private String serviceName;
    private String description;
    private Double basePrice;
    private Double gstPercentage;

}
