package com.arka.microservice.domain.model;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Warehouse {
    private String warehouseId;
    private String name;
    private String city;
    private String country;
    private String address;
    private String phone;
    private LocalDateTime createdAt;
}
