package com.arka.proveedor.model;

import com.arka.proveedor.entity.CompraDetalle;
import com.arka.proveedor.entity.CompraProveedor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompraRequest {
    @Valid
    @NotNull(message = "La compraProveedor no puede ser nula")
    private CompraProveedor compraProveedor;

    @Valid
    @NotEmpty(message = "Debe incluir al menos un detalle de compra")
    private List<CompraDetalle> compraDetalles;
}
