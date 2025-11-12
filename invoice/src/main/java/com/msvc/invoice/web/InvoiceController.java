package com.msvc.invoice.web;


import com.msvc.invoice.dto.InvoiceResponse;
import com.msvc.invoice.mapper.InvoiceMapper;
import com.msvc.invoice.repositories.InvoiceItemRepository;
import com.msvc.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceItemRepository itemRepo;

    public InvoiceController(InvoiceService service, InvoiceItemRepository itemRepo) {
        this.service = service;
        this.itemRepo = itemRepo;
    }

    @PostMapping("/from-order/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<InvoiceResponse> createFromOrder(@PathVariable String orderId) {
        return service.createFromOrder(orderId)
                .flatMap(inv -> itemRepo.findByInvoiceId(inv.getId()).collectList()
                        .map(items -> InvoiceMapper.toInvoiceResponse(inv, items)));
    }

    @GetMapping("/{invoiceId}")
    public Mono<InvoiceResponse> getById(@PathVariable String invoiceId) {
        return service.getById(invoiceId)
                .flatMap(inv -> itemRepo.findByInvoiceId(inv.getId()).collectList()
                        .map(items -> InvoiceMapper.toInvoiceResponse(inv, items)));
    }
}
