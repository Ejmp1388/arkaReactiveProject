package com.msvc.shopcart.dto;
import jakarta.validation.constraints.NotBlank;

public class CreateCartRequest {
    @NotBlank
    private String customerId;
    private String currency = "USD";

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }


}

