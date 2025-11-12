package com.msvc.shopcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProductInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("stock")
    private BigDecimal stock;

    public String getId() {
        return id;
    }

    public BigDecimal getStock() {
        return stock;
    }
}