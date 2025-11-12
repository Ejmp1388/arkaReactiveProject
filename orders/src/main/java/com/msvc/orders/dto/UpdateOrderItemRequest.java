package com.msvc.orders.dto;

import java.math.BigDecimal;

public class UpdateOrderItemRequest {
    private BigDecimal quantity;
    private BigDecimal unitPrice;

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}

