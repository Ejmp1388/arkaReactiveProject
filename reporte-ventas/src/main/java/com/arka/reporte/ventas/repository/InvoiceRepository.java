package com.arka.reporte.ventas.repository;

import com.arka.reporte.ventas.entity.Invoice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface InvoiceRepository extends ReactiveCrudRepository<Invoice, String> {
    Flux<Invoice> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
