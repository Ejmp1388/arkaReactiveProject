package com.arka.reporte.ventas.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("invoices")
@Getter
@Setter
public class Invoice {
    @Id
    private String id;
    private String orderId;
    private String customerId;
    private String invoiceNumber;
    private String status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
