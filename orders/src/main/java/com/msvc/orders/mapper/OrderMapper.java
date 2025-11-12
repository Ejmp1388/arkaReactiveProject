package com.msvc.orders.mapper;



import com.msvc.orders.domain.Order;
import com.msvc.orders.domain.OrderItem;
import com.msvc.orders.dto.OrderItemResponse;
import com.msvc.orders.dto.OrderResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderItemResponse toItemResponse(OrderItem it) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setId(it.getId());
        dto.setProductId(it.getProductId());
        dto.setProductName(it.getProductName());
        dto.setUnitPrice(it.getUnitPrice());
        dto.setQuantity(it.getQuantity());
        dto.setLineTotal(it.getLineTotal());
        return dto;
    }

    public static OrderResponse toOrderResponse(Order o, List<OrderItem> items) {
        OrderResponse dto = new OrderResponse();
        dto.setId(o.getId());
        dto.setCustomerId(o.getCustomerId());
        dto.setCartId(o.getCartId());
        dto.setStatus(o.getStatus());
        dto.setCurrency(o.getCurrency());
        dto.setSubtotal(o.getSubtotal());
        dto.setTaxes(o.getTaxes());
        dto.setTotal(o.getTotal());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setUpdatedAt(o.getUpdatedAt());
        dto.setItems(items.stream().map(OrderMapper::toItemResponse).collect(Collectors.toList()));
        return dto;
    }
}
