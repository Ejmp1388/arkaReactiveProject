package com.arka.microservice.infrastructure.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("warehouse")
public class WarehouseEntity {

    @Id
    @Column("warehouse_id")
    private String warehouseId;

    private String name;

    private String city;

    private String country;

    private String address;

    private String phone;

    @Column("created_at")
    private LocalDateTime createdAt;
}
