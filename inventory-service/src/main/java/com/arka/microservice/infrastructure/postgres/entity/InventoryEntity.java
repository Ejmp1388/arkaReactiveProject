package com.arka.microservice.infrastructure.postgres.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("inventory")
public class InventoryEntity {
    @Id
    @Column("inventory_id")
    private String inventoryId;

    @Column("product_id")
    private String productId;

    @Column("warehouse_id")
    private String warehouseId;

    @Column("available_quantity")
    private Integer availableQuantity;

    @Column("reserved_quantity")
    private Integer reservedQuantity;

    @Column("minimum_quantity")
    private Integer minimumQuantity;

    @Column("updated_at")
    private LocalDateTime updateAt;
}
