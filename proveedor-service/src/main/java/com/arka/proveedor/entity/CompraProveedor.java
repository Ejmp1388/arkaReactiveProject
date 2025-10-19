package com.arka.proveedor.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("purchase_supplier")
@Getter
@Setter
public class CompraProveedor {
    @Id
    private String id;

    @NotNull(message = "El Id de proveedor es obligatorio")
    @Column("supplier_id")
    private String supplierId;

    private Boolean active;

    //@NotNull(message = "El total es obligatorio")
    //@DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    private BigDecimal total;

    //@NotNull(message = "La fecha de compra es obligatoria")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
