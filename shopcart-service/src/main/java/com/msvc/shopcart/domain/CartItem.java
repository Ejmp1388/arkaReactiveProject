package com.msvc.shopcart.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("cart_items")
public class CartItem {

    @Id
    private String id;                  // <— UUID autogenerado en BD
    @Column("cart_id")
    private String cartId;

    @Column("product_id")
    private String productId;
    private String name;
    private int quantity;

    @Column("unit_price_cents")
    private long unitPriceCents;

    public CartItem() { }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public long getUnitPriceCents() { return unitPriceCents; }
    public void setUnitPriceCents(long unitPriceCents) { this.unitPriceCents = unitPriceCents; }
}
