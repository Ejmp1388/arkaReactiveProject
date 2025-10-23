package com.arka.reporte.controller;

import com.arka.reporte.dto.LowStockInventoryDTO;
import com.arka.reporte.entity.Inventory;
import com.arka.reporte.service.InventoryService;
import com.arka.reporte.util.CsvExporter;
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

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reporte")
public class InventoryController {

    private final InventoryService service;
    private final CsvExporter csvExporter;

    @GetMapping("/low-stock")
    public Flux<LowStockInventoryDTO> getLowStockItems() {
        return service.getLowStockItems();
    }

    @GetMapping(value = "/low-stock/export", produces = "text/csv")
    public Mono<ResponseEntity<Resource>> exportLowStockCsv() {
        return service.getLowStockItems()
                .collectList()
                .map(csvExporter::generateInventoryCsv)
                .map(csv -> {
                    ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=low_stock_inventory.csv")
                            .contentType(MediaType.parseMediaType("text/csv"))
                            .body(resource);
                });
    }

}
