package com.arka.reporte.util;

import com.arka.reporte.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class InventoryReportScheduler {

    private final InventoryService inventoryService;
    private final CsvExporter csvExporter;

    @Scheduled(fixedRateString = "#{@reportProperties.interval}")
    public void generateCsvReport() {
        inventoryService.getLowStockItems()
                .collectList()
                .map(csvExporter::generateInventoryCsv)
                .subscribe(csv -> {
                    // Aquí se puede guardar el archivo en disco
                    Path path = Paths.get("low_stock_inventory.csv");
                    try {
                        Files.write(path, csv.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Reporte generado: " + path.toAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
