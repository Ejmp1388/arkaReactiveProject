package com.arka.microservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryAvailabilityRequestDTO {
    private String productId;
    private String warehouseId;
}
