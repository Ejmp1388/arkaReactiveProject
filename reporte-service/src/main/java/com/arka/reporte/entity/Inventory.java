package com.arka.reporte.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("inventory")
@Getter
@Setter
public class Inventory {
    @Id
    private String inventoryId;

    private String productId;
    private String warehouseId;
    private int availableQuantity;
    private int reserveredQuantity;
    private int minimumQuantity;
    private LocalDateTime updatedAt;
}
