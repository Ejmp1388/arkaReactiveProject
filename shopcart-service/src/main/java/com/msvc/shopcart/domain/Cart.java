package com.msvc.shopcart.domain;

import com.msvc.shopcart.util.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Table("carts")
public class Cart {

    @Id
    private String id;
    @Column("customer_id")
    private String customerId;
    @Column("checked_out")
    private boolean checkedOut;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;

    @Transient
    private List<CartItem> items = new ArrayList<>();

    public Cart() { }

    public Cart(String customerId) {         // <— constructor sin id
        this.customerId = customerId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.checkedOut = false;
    }

    public Money total() {
        long cents = items.stream()
                .mapToLong(i -> i.getUnitPriceCents() * i.getQuantity())
                .sum();
        return Money.ofCents(cents, "USD");
    }

    public void touch() { this.updatedAt = Instant.now(); }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public boolean isCheckedOut() { return checkedOut; }
    public void setCheckedOut(boolean checkedOut) { this.checkedOut = checkedOut; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
