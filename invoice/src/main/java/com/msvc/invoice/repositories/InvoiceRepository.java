package com.msvc.invoice.repositories;


import com.msvc.invoice.domain.Invoice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface InvoiceRepository extends ReactiveCrudRepository<Invoice, String> {
    Mono<Boolean> existsByOrderId(String orderId);
    Mono<Invoice> findByOrderId(String orderId);
}

