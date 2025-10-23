package com.arka.reporte.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LowStockInventoryDTO {
    private String inventoryId;
    private String productId;
    private String warehouseId;
    private String warehouseName;
    private int availableQuantity;
    private int reserveredQuantity;
    private int minimumQuantity;
    private LocalDateTime updatedAt;
}
