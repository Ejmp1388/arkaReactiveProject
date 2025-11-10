package com.arka.proveedor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateRequest {
    private String productId;
    private String warehouseId;
    private Integer quantity;
    private String type;
    private String note;

    public InventoryUpdateRequest() {}

    public InventoryUpdateRequest(String productId, String warehouseId, Integer quantity, String type, String note) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.type = type;
        this.note = note;
    }
}
