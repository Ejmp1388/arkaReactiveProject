package com.arka.reporte.ventas.util;

import com.arka.reporte.ventas.dto.ReporteSemanal;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class CsvExporter {
    public ByteArrayResource exportarReporteSemanal(List<ReporteSemanal> reportes) {
        StringBuilder sb = new StringBuilder();

        // Encabezado
        sb.append("Semana,Sección,Clave,Valor\n");

        for (int i = 0; i < reportes.size(); i++) {
            ReporteSemanal reporte = reportes.get(i);
            String semana = "Semana " + (i + 1);

            // Total de ventas
            sb.append(semana).append(",TotalVentas,,").append(reporte.getTotalVentas()).append("\n");

            // Productos más vendidos
            reporte.getProductosMasVendidos().forEach((producto, cantidad) -> {
                sb.append(semana).append(",Producto,")
                        .append(escapeCsv(producto)).append(",")
                        .append(cantidad).append("\n");
            });

            // Clientes más frecuentes
            reporte.getClientesMasFrecuentes().forEach((cliente, frecuencia) -> {
                sb.append(semana).append(",Cliente,")
                        .append(escapeCsv(cliente)).append(",")
                        .append(frecuencia).append("\n");
            });
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new ByteArrayResource(bytes);
    }

    // Escapa comas y comillas dobles para formato CSV válido
    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
