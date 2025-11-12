package com.msvc.invoice.integration;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDtos {
    public static class OrderItemDTO {
        public String id;
        public String productId;
        public String productName;
        public BigDecimal unitPrice;
        public BigDecimal quantity;
        public BigDecimal lineTotal;
    }

    public static class OrderResponseDTO {
        public String id;
        public String customerId;
        public String cartId;
        public String status;
        public String currency;
        public BigDecimal subtotal;
        public BigDecimal taxes;
        public BigDecimal total;
        public LocalDateTime createdAt;
        public LocalDateTime updatedAt;
        public List<OrderItemDTO> items;
    }
}
