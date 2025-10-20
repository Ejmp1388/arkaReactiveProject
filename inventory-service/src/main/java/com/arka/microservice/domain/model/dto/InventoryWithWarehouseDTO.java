package com.arka.microservice.domain.model.dto;

import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryWithWarehouseDTO {
    private String inventoryId;
    private String productId;
    private String warehouseId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer minimumQuantity;

    private String warehouseName;
    private String warehouseCity;
    private String warehouseCountry;
    private String warehouseAddress;
    private String warehousePhone;
}
