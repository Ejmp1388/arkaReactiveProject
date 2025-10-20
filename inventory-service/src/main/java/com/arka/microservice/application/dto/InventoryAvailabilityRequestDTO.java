package com.arka.microservice.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAvailabilityRequestDTO {
    private String productId;
    private String warehouseId;
}
