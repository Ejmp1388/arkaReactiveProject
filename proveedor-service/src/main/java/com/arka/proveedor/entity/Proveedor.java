package com.arka.proveedor.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("suppliers")
@Getter
@Setter
public class Proveedor {
    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private String identification;

    @NotBlank
    private String address;

    @NotBlank
    private String contact;

    @Email
    private String email;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
