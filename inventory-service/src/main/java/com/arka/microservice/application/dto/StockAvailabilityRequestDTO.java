package com.arka.microservice.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockAvailabilityRequestDTO {
    private String warehouseId;
    private List<ProductRequestDTO> productos;
}
