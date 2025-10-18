package com.msvc.shopcart.dto;


import com.msvc.shopcart.domain.Cart;
import com.msvc.shopcart.util.Money;

public class CheckoutResponse {
    private String cartId;
    private String customerId;
    private Money total;
    private String orderId; // si generas la orden en otro ms, retorna el id

    public CheckoutResponse() {}

    public CheckoutResponse(Cart cart, String orderId) {
        this.cartId = cart.getId();
        this.customerId = cart.getCustomerId();
        this.total = cart.total();
        this.orderId = orderId;
    }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public Money getTotal() { return total; }
    public void setTotal(Money total) { this.total = total; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}

