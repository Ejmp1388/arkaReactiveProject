package com.arka.proveedor.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("proveedores")
@Getter
@Setter
public class Proveedor {
    @Id
    private Long id;
    private String name;
    private String type;
    private String identification;
    private String address;
    private String contact;
    private String email;
    private String status;

}
