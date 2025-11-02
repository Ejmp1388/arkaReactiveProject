package com.arka.reporte.ventas.repository;

import com.arka.reporte.ventas.entity.InvoiceItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface InvoiceItemRepository extends ReactiveCrudRepository<InvoiceItem, String>{
    Flux<InvoiceItem> findByInvoiceIdIn(List<String> invoiceIds);
}
