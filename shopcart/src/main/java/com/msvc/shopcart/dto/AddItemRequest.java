package com.msvc.shopcart.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AddItemRequest {
    @NotBlank private String productId;
    @NotBlank private String productName;
    @NotNull @DecimalMin("0.00") private BigDecimal unitPrice;
    @NotNull @DecimalMin("0.001") private BigDecimal quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}

