package com.arka.reporte.ventas.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ReporteSemanal {
    private BigDecimal totalVentas;
    private Map<String, Long> productosMasVendidos;
    private Map<String, Long> clientesMasFrecuentes;

    public ReporteSemanal(BigDecimal totalVentas,
                          Map<String, Long> productosMasVendidos,
                          Map<String, Long> clientesMasFrecuentes) {
        this.totalVentas = totalVentas;
        this.productosMasVendidos = productosMasVendidos;
        this.clientesMasFrecuentes = clientesMasFrecuentes;
    }
}
