package com.arka.microservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockHistory {
    private String historyId;
    private String inventoryId;
    private String adjustmentType;
    private int quantityBefore;
    private int quantityAfter;
    private String note;
    private LocalDateTime createdAt;
}
