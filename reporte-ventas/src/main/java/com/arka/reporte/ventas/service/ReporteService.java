package com.arka.reporte.ventas.service;

import com.arka.reporte.ventas.dto.ReporteSemanal;
import com.arka.reporte.ventas.entity.Customer;
import com.arka.reporte.ventas.entity.Invoice;
import com.arka.reporte.ventas.entity.InvoiceItem;
import com.arka.reporte.ventas.repository.CustomerRepository;
import com.arka.reporte.ventas.repository.InvoiceItemRepository;
import com.arka.reporte.ventas.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final CustomerRepository customerRepository;

    public Flux<ReporteSemanal> generarReporte(LocalDateTime inicio, LocalDateTime fin) {
        return invoiceRepository.findByCreatedAtBetween(inicio, fin)
                .collectList()
                .flatMapMany(invoices -> {
                    List<String> invoiceIds = invoices.stream().map(Invoice::getId).toList();
                    List<String> customerIds = invoices.stream().map(Invoice::getCustomerId).toList();
                    BigDecimal totalVentas = invoices.stream().map(Invoice::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

                    return invoiceItemRepository.findByInvoiceIdIn(invoiceIds)
                            .collectList()
                            .flatMap(items -> {
                                Map<String, Long> productos = items.stream()
                                        .collect(Collectors.groupingBy(InvoiceItem::getProductName, Collectors.counting()));

                                return customerRepository.findByIdIn(customerIds)
                                        .collectList()
                                        .map(clientes -> {
                                            Map<String, Long> clientesFrecuentes = clientes.stream()
                                                    .collect(Collectors.groupingBy(Customer::getName, Collectors.counting()));

                                            return new ReporteSemanal(totalVentas, productos, clientesFrecuentes);
                                        });
                            });
                });
    }



}
