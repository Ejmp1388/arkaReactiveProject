package com.msvc.invoice.service;

import com.msvc.invoice.domain.Invoice;
import com.msvc.invoice.domain.InvoiceItem;
import com.msvc.invoice.domain.InvoiceStatus;
import com.msvc.invoice.exception.ConflictException;
import com.msvc.invoice.exception.NotFoundException;
import com.msvc.invoice.integration.OrderDtos;
import com.msvc.invoice.repositories.InvoiceItemRepository;
import com.msvc.invoice.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final InvoiceItemRepository itemRepo;
    private final TransactionalOperator tx;
    private final WebClient orderClient;

    public InvoiceService(InvoiceRepository invoiceRepo,
                          InvoiceItemRepository itemRepo,
                          ReactiveTransactionManager rtm,
                          WebClient.Builder webClientBuilder,
                          @Value("${app.orders.base-url}") String ordersBaseUrl) {
        this.invoiceRepo = invoiceRepo;
        this.itemRepo = itemRepo;
        this.tx = TransactionalOperator.create(rtm);
        this.orderClient = webClientBuilder.baseUrl(ordersBaseUrl).build();
    }

    public Mono<Invoice> getById(String invoiceId) {
        return invoiceRepo.findById(invoiceId)
                .switchIfEmpty(Mono.error(new NotFoundException("Factura no encontrada: " + invoiceId)));
    }


    private Mono<OrderDtos.OrderResponseDTO> fetchOrder(String orderId) {
        return orderClient.get()
                .uri("/api/v1/orders/{id}", orderId)  // necesitas este GET en Orders
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)
                                ? Mono.error(new NotFoundException("Order no encontrada: " + orderId))
                                : resp.createException().flatMap(Mono::error)
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(OrderDtos.OrderResponseDTO.class);
    }

    /** Factura a partir de una orden de venta */
    public Mono<Invoice> createFromOrder(String orderId) {
        return invoiceRepo.existsByOrderId(orderId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<OrderDtos.OrderResponseDTO>error(
                                new ConflictException("Ya existe una factura para la orden: " + orderId)
                        );
                    }
                    return fetchOrder(orderId);
                })
                .flatMap(order -> {
                    // regla de negocio: solo facturo si la orden está APPROVED o DELIVERED
                    if (!Objects.equals(order.status, "APPROVED") &&
                            !Objects.equals(order.status, "DELIVERED")) {
                        return Mono.error(new ConflictException(
                                "La orden no está en un estado facturable: " + order.status));
                    }

                    // generar número de factura simple (luego lo puedes cambiar a secuencia)
                    String invoiceNumber = "F-" + System.currentTimeMillis();

                    Invoice inv = new Invoice();
                    inv.setOrderId(order.id);
                    inv.setCustomerId(order.customerId);
                    inv.setInvoiceNumber(invoiceNumber);
                    inv.setStatus(InvoiceStatus.CREATED);
                    inv.setCurrency(order.currency);
                    inv.setSubtotal(order.subtotal);
                    inv.setTaxes(order.taxes);
                    inv.setTotal(order.total);

                    return invoiceRepo.save(inv)
                            .flatMap(saved ->
                                    Flux.fromIterable(order.items)
                                            .flatMap(oi -> {
                                                InvoiceItem ii = new InvoiceItem();
                                                ii.setId(null);
                                                ii.setInvoiceId(saved.getId());
                                                ii.setProductId(oi.productId);
                                                ii.setProductName(oi.productName);
                                                ii.setUnitPrice(oi.unitPrice);
                                                ii.setQuantity(oi.quantity);
                                                ii.setLineTotal(oi.lineTotal);
                                                return itemRepo.save(ii);
                                            })
                                            .then(Mono.just(saved))
                            );
                })
                .as(tx::transactional);
    }
}

