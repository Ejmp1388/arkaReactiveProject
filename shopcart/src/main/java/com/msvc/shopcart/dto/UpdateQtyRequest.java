package com.msvc.shopcart.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class UpdateQtyRequest {
    @NotNull @DecimalMin("0.001")
    private BigDecimal quantity;
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
