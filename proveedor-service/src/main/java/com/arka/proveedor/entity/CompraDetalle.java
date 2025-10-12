package com.arka.proveedor.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("compra_detalle")
@Getter
@Setter
public class CompraDetalle {
    @Id
    private Long id;
    private Long idPurcharse;

    @NotNull(message = "El id_product es obligatorio")
    private Integer idProduct;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que 0")
    private BigDecimal unitPrice;
}
