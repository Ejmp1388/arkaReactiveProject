package com.msvc.shopcart.dto;

import jakarta.validation.constraints.NotBlank;

public class RemoveItemRequest {
    @NotBlank private String productId;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}
