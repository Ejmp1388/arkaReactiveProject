package com.arka.reporte.ventas.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("invoice_items")
@Getter
@Setter
public class InvoiceItem {
    @Id
    private String id;
    private String invoiceId;
    private String productId;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal lineTotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
