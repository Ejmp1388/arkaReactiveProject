package com.msvc.shopcart.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("carts")
public class Cart {
    @Id
    private String id;
    private String customerId;
    private String status; // OPEN | CONFIRMED | CANCELED
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart() {}

    public Cart(String customerId, String status, String currency,
                BigDecimal subtotal, BigDecimal taxes, BigDecimal total,
                LocalDateTime updatedAt) {
        //this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.currency = currency;
        this.subtotal = subtotal;
        this.taxes = taxes;
        this.total = total;
        //this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
