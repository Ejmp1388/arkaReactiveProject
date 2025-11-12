package com.msvc.shopcart.mapper;



import com.msvc.shopcart.domain.Cart;
import com.msvc.shopcart.domain.CartItem;
import com.msvc.shopcart.dto.CartItemResponse;
import com.msvc.shopcart.dto.CartResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartItemResponse toItemResponse(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setQuantity(item.getQuantity());
        dto.setLineTotal(item.getLineTotal());
        return dto;
    }

    public static CartResponse toCartResponse(Cart cart, List<CartItem> items) {
        CartResponse dto = new CartResponse();
        dto.setId(cart.getId());
        dto.setCustomerId(cart.getCustomerId());
        dto.setStatus(cart.getStatus());
        dto.setCurrency(cart.getCurrency());
        dto.setSubtotal(cart.getSubtotal());
        dto.setTaxes(cart.getTaxes());
        dto.setTotal(cart.getTotal());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        dto.setItems(items.stream().map(CartMapper::toItemResponse).collect(Collectors.toList()));
        return dto;
    }
}
