package com.arka.reporte.ventas.controller;

import com.arka.reporte.ventas.dto.ReporteSemanal;
import com.arka.reporte.ventas.service.ReporteService;
import com.arka.reporte.ventas.util.CsvExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reporte")
public class ReporteController {

    private final ReporteService reporteService;
    private final CsvExporter csvExporter;

    @GetMapping("/semanal")
    public Flux<ReporteSemanal> obtenerReporte() {
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime inicio = fin.minusWeeks(1);
        return reporteService.generarReporte(inicio, fin);
    }

    @GetMapping(value = "/semanal/csv", produces = "text/csv")
    public Mono<ResponseEntity<Resource>> exportarCSV() {
        //return reporteService.generarReporteCSV();
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime inicio = fin.minusWeeks(1);
        return  reporteService.generarReporte(inicio, fin)
                .collectList()
                .map(reporte ->{
                    ByteArrayResource resource = csvExporter.exportarReporteSemanal(reporte);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_semanal.csv")
                            .contentType(MediaType.parseMediaType("text/csv"))
                            .contentLength(resource.contentLength())
                            .body(resource);
                });
    }
}
