package com.arka.microservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StockAvailabilityRequestDTO {
    private String warehouseId;
    private List<ProductRequestDTO> productos;
}
