package com.msvc.orders.dto;

import java.math.BigDecimal;
public record InventoryAdjustmentRequest(
        String productId,
        String warehouseId,
        BigDecimal quantity,
        String type,
        String note
) {}