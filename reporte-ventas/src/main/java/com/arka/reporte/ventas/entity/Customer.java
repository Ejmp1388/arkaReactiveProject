package com.arka.reporte.ventas.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("customers")
@Getter
@Setter
public class Customer {
    @Id
    private String id;
    private String name;
    private String country;
    private String state;
    private String district;
    private String city;
    private String address;
    private String phone;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
