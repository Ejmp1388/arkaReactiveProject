package com.arka.microservice.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockUpdateRequest {
    private String productId;
    private String warehouseId;
    private int quantity;
    private String type; // INCREASE, DECREASE, RESERVE, RELEASE
    private String note;
}
