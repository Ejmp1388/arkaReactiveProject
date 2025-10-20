package com.arka.microservice.infrastructure.postgres.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("stock_history")
public class StockHistoryEntity {
    @Id
    @Column("history_id")
    private String historyId;

    @Column("inventory_id")
    private String inventoryId;

    @Column("adjustment_type")
    private String adjustmentType;

    @Column("quantity_before")
    private Integer quantityBefore;

    @Column("quantity_after")
    private Integer quantityAfter;

    private String note;

    @Column("created_at")
    private LocalDateTime createdAt;
}
