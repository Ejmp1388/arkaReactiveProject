package com.arka.microservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
    private String inventoryId;
    private String productId;    // productId _id desde Mongo
    private String warehouseId;
    private int availableQuantity;
    private int reservedQuantity;
    private int minimumQuantity;
    private LocalDateTime updateAt;
}