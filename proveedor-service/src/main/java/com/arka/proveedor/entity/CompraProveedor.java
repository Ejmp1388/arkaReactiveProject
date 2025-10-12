package com.arka.proveedor.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("compra_proveedor")
@Getter
@Setter
public class CompraProveedor {
    @Id
    private Long id;

    @NotNull(message = "El id_proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDateTime purcharseDate;

    @Size(max = 10, message = "El estado no puede tener más de 10 caracteres")
    private String status;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    private BigDecimal total;
}
