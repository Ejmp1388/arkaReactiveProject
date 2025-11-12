package com.msvc.orders.integration;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartDtos {
    public static class CartItemDTO {
        public String id;
        public String productId;
        public String productName;
        public BigDecimal unitPrice;
        public BigDecimal quantity;
        public BigDecimal lineTotal;
    }
    public static class CartResponseDTO {
        public String id;
        public String customerId;
        public String status;
        public String currency;
        public BigDecimal subtotal;
        public BigDecimal taxes;
        public BigDecimal total;
        public LocalDateTime createdAt;
        public LocalDateTime updatedAt;
        public List<CartItemDTO> items;
    }
}

