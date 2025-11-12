package com.msvc.invoice.repositories;


import com.msvc.invoice.domain.InvoiceItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface InvoiceItemRepository extends ReactiveCrudRepository<InvoiceItem, String> {
    Flux<InvoiceItem> findByInvoiceId(String invoiceId);
}

