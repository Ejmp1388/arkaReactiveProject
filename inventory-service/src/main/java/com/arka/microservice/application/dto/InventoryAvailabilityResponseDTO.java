package com.arka.microservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryAvailabilityResponseDTO {
    private String productId;
    private String warehouseId;
    private Integer availableQuantity;
}
