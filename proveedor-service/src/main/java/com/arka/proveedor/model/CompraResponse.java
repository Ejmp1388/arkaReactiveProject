package com.arka.proveedor.model;

import com.arka.proveedor.entity.CompraDetalle;
import com.arka.proveedor.entity.CompraProveedor;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompraResponse {
    
    private CompraProveedor compraProveedor;


    private List<CompraDetalle> compraDetalles;
}
