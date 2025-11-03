package com.arka.microservice.infrastructure.cli.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockRequest {
    private String productId;
    private String warehouseId;
    private int quantity;
    private String type; // INCREASE, DECREASE, RESERVE, RELEASE,(OTRO)
    private String note;
}
