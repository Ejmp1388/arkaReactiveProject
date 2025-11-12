package com.arka.reporte.util;

import com.arka.reporte.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Component
@RequiredArgsConstructor
public class InventoryReportScheduler {

    private final InventoryService inventoryService;
    private final CsvExporter csvExporter;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    //@Scheduled(fixedRateString = "#{@reportProperties.interval}")
    // Ejecuta cada 1 días (configurable con reportProperties.weeklyCron)
    @Scheduled(cron = "${reportProperties.weeklyCron}")
    public void generateCsvReport() {
        inventoryService.getLowStockItems()
                .collectList()
                .map(csvExporter::generateInventoryCsv)
                .subscribe(csv -> {
                    String key = "low_stock_inventory.csv";
                    // Aquí se puede guardar el archivo en disco
                    //Path path = Paths.get("low_stock_inventory.csv");
                    s3Client.putObject(PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(key)
                                    .contentType("text/csv")
                                    .build(),
                    RequestBody.fromBytes(csv.getBytes(StandardCharsets.UTF_8)));
                    System.out.println("Reporte subido a S3: s3://" + bucketName + "/" + key);
                    //Files.write(path, csv.getBytes(StandardCharsets.UTF_8));
                    //System.out.println("Reporte generado: " + path.toAbsolutePath());
                });
    }

}
