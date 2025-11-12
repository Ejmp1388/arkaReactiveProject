package com.msvc.orders.service;

import com.msvc.orders.domain.ApproveOrderRequest;
import com.msvc.orders.domain.Order;
import com.msvc.orders.domain.OrderItem;
import com.msvc.orders.domain.OrderStatus;
import com.msvc.orders.dto.InventoryAdjustmentRequest;
import com.msvc.orders.exception.NotFoundException;
import com.msvc.orders.exception.ConflictException;
import com.msvc.orders.integration.CartDtos;
import com.msvc.orders.repository.OrderItemRepository;
import com.msvc.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final TransactionalOperator tx;
    private final WebClient cartClient;
    private final NotificationService notificationService;
    private final WebClient emailClient;
    private final WebClient inventoryClient;



    public Mono<Order> getById(String orderId) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada: " + orderId)));
    }

    /*
    public OrderService(OrderRepository orderRepo,
                        OrderItemRepository itemRepo,
                        ReactiveTransactionManager rtm,
                        WebClient.Builder webClientBuilder,
                        @Value("${app.cart.base-url}") String cartBaseUrl) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.tx = TransactionalOperator.create(rtm);
        this.cartClient = webClientBuilder.baseUrl(cartBaseUrl).build();
    }
    */

    public OrderService(OrderRepository orderRepo,
                        OrderItemRepository itemRepo,
                        ReactiveTransactionManager rtm,
                        WebClient.Builder webClientBuilder,
                        NotificationService notificationService,
                        @Value("${app.cart.base-url}") String cartBaseUrl,
                        @Value("${app.customer.base-url}") String emailBaseUrl,
                        @Value("${app.inventory.base-url}") String inventoryBaseUrl
                        ) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.tx = TransactionalOperator.create(rtm);
        this.cartClient = webClientBuilder.baseUrl(cartBaseUrl).build();
        this.notificationService = notificationService;
        this.emailClient = webClientBuilder.baseUrl(emailBaseUrl).build();
        this.inventoryClient = webClientBuilder.baseUrl(inventoryBaseUrl).build();
    }

    /** Llama al MS de Cart y trae el CartResponseDTO */
    private Mono<CartDtos.CartResponseDTO> fetchCart(String cartId) {
        return cartClient.get()
                .uri("/api/v1/carts/{cartId}", cartId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)
                                ? Mono.error(new NotFoundException("Cart no encontrado: " + cartId))
                                : resp.createException().flatMap(Mono::error)
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(CartDtos.CartResponseDTO.class);
    }

    /** Crea una orden de venta a partir de un Cart en estado CONFIRMED */
    public Mono<Order> createFromConfirmedCart(String cartId) {
        Mono<Order> txFlow = orderRepo.existsByCartId(cartId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<CartDtos.CartResponseDTO>
                                error(new ConflictException("Ya existe una orden para el cart: " + cartId));
                    }
                    return fetchCart(cartId); // Mono<CartResponseDTO>
                })
                .flatMap(cart -> {
                    if (!Objects.equals(cart.status, "CONFIRMED")) {
                        return Mono.error(new ConflictException("Cart debe estar CONFIRMED para generar orden"));
                    }
                    Order order = new Order();
                    order.setCustomerId(cart.customerId);
                    order.setCartId(cart.id);
                    order.setStatus(OrderStatus.PENDING);
                    order.setCurrency(cart.currency);
                    order.setSubtotal(cart.subtotal);
                    order.setTaxes(cart.taxes);
                    order.setTotal(cart.total);

                    return orderRepo.save(order)
                            .flatMap(saved ->
                                    Flux.fromIterable(cart.items)
                                            .flatMap(ci -> {
                                                OrderItem oi = new OrderItem();
                                                oi.setId(null); // generado por la BD
                                                oi.setOrderId(saved.getId());
                                                oi.setProductId(ci.productId);
                                                oi.setProductName(ci.productName);
                                                oi.setUnitPrice(ci.unitPrice);
                                                oi.setQuantity(ci.quantity);
                                                oi.setLineTotal(ci.lineTotal);
                                                return itemRepo.save(oi);
                                            })
                                            .then(Mono.just(saved))
                            );
                })
                .as(tx::transactional);


        return txFlow.flatMap(savedOrder ->
                getCustomerEmail(savedOrder.getCustomerId())
                        .flatMap(email -> {
                            String subject = "Orden " + savedOrder.getId() + " creada";
                            String body = "Hola, tu orden fue creada a partir del carrito "
                                    + savedOrder.getCartId()
                                    + " con estado: " + savedOrder.getStatus()
                                    + " y total: " + savedOrder.getTotal();

                            // Llamada al API Gateway (Lambda)
                            return notificationService.sendEmail(email, subject, body)
                                    .thenReturn(savedOrder);
                        })
                        // Si no se pudo enviar el correo, no romper la creación de la orden
                        .onErrorResume(e -> {
                            System.err.println("No se pudo enviar correo para la orden " + savedOrder.getId()
                                    + ": " + e.getMessage());
                            return Mono.just(savedOrder);
                        })
        );
    }

    // ====== actualizar cantidad/precio de un item v1 ======
    /*
        public Mono<Order> updateOrderItem(String orderId, String itemId,
                                           BigDecimal newQty, BigDecimal newUnitPrice) {

            return orderRepo.findById(orderId)
                    .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada: " + orderId)))
                    .flatMap(order -> {
                        // regla: solo editable en NEW o PENDING
                        if (!OrderStatus.NEW.equals(order.getStatus())
                                && !OrderStatus.PENDING.equals(order.getStatus())) {
                            return Mono.error(new ConflictException(
                                    "La orden no se puede editar en estado: " + order.getStatus()));
                        }

                        // buscar el item
                        return itemRepo.findById(itemId)
                                .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                                .flatMap(item -> {
                                    // validar que el item pertenece a la orden
                                    if (!orderId.equals(item.getOrderId())) {
                                        return Mono.error(new ConflictException("El item no pertenece a la orden"));
                                    }

                                    // aplicar cambios
                                    if (newQty != null) {
                                        if (newQty.compareTo(BigDecimal.ZERO) <= 0) {
                                            return Mono.error(new IllegalArgumentException("La cantidad debe ser > 0"));
                                        }
                                        item.setQuantity(newQty);
                                    }
                                    if (newUnitPrice != null) {
                                        if (newUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
                                            return Mono.error(new IllegalArgumentException("El precio no puede ser negativo"));
                                        }
                                        item.setUnitPrice(newUnitPrice);
                                    }

                                    // recalcular línea
                                    item.setLineTotal(item.getUnitPrice().multiply(item.getQuantity()));
                                    item.setUpdatedAt(LocalDateTime.now());

                                    return itemRepo.save(item)
                                            .then(recalculateOrderTotals(orderId));  // recalculamos la orden
                                });
                    })
                    .as(tx::transactional);
        }
        */

    public Mono<Order> updateOrderItem(String orderId, String itemId,
                                       BigDecimal newQty, BigDecimal newUnitPrice) {

        String warehouseId = "31dd5c63-86fa-49a3-ae77-c55f203dec4b";

        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada: " + orderId)))
                .flatMap(order -> {
                    // regla: solo editable en NEW o PENDING
                    if (!OrderStatus.NEW.equals(order.getStatus())
                            && !OrderStatus.PENDING.equals(order.getStatus())) {
                        return Mono.error(new ConflictException(
                                "La orden no se puede editar en estado: " + order.getStatus()));
                    }

                    // buscar el item
                    return itemRepo.findById(itemId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                            .flatMap(item -> {
                                // validar que el item pertenece a la orden
                                if (!orderId.equals(item.getOrderId())) {
                                    return Mono.error(new ConflictException("El item no pertenece a la orden"));
                                }

                                BigDecimal oldQty = item.getQuantity();
                                BigDecimal delta = (newQty != null)
                                        ? newQty.subtract(oldQty)
                                        : BigDecimal.ZERO;

                                // aplicar cambios
                                if (newQty != null) {
                                    if (newQty.compareTo(BigDecimal.ZERO) <= 0) {
                                        return Mono.error(new IllegalArgumentException("La cantidad debe ser > 0"));
                                    }
                                    item.setQuantity(newQty);
                                }
                                if (newUnitPrice != null) {
                                    if (newUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
                                        return Mono.error(new IllegalArgumentException("El precio no puede ser negativo"));
                                    }
                                    item.setUnitPrice(newUnitPrice);
                                }

                                // recalcular línea
                                item.setLineTotal(item.getUnitPrice().multiply(item.getQuantity()));
                                item.setUpdatedAt(LocalDateTime.now());

                                return itemRepo.save(item)
                                        .flatMap(savedItem -> {
                                            // si la cantidad no cambió, no llamamos a inventory
                                            if (delta.signum() == 0) {
                                                return recalculateOrderTotals(orderId);
                                            }

                                            // construir request para Inventory
                                            InventoryAdjustmentRequest req = new InventoryAdjustmentRequest(
                                                    savedItem.getProductId(),
                                                    warehouseId,
                                                    delta.abs(),
                                                    (delta.signum() > 0) ? "RESERVE" : "RELEASE",
                                                    (delta.signum() > 0)
                                                            ? "Se incrementa cantidad pedida"
                                                            : "Se disminuye cantidad pedida"
                                            );

                                            System.out.println(req);

                                            // llamar al servicio de Inventory y luego recalcular
                                            return callInventory(req)
                                                    .then(recalculateOrderTotals(orderId));
                                        });
                            });
                })
                .as(tx::transactional);
    }



    private Mono<Void> callInventory(InventoryAdjustmentRequest req) {
        return inventoryClient.post()
                .uri("/api/inventory/update")
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, cr ->
                        cr.bodyToMono(String.class)
                                .defaultIfEmpty("Solicitud inválida a Inventory")
                                .flatMap(msg -> Mono.error(new ConflictException("Inventory 4xx: " + msg)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, cr ->
                        Mono.error(new RuntimeException("Inventory 5xx"))
                )
                .toBodilessEntity()
                .then();
    }


    // ====== eliminar un item ======
    /*
    public Mono<Order> removeOrderItem(String orderId, String itemId) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada: " + orderId)))
                .flatMap(order -> {
                    if (!OrderStatus.NEW.equals(order.getStatus())
                            && !OrderStatus.PENDING.equals(order.getStatus())) {
                        return Mono.error(new ConflictException(
                                "La orden no se puede editar en estado: " + order.getStatus()));
                    }

                    return itemRepo.findById(itemId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                            .flatMap(item -> {
                                if (!orderId.equals(item.getOrderId())) {
                                    return Mono.error(new ConflictException("El item no pertenece a la orden"));
                                }
                                return itemRepo.deleteById(itemId)
                                        .then(recalculateOrderTotals(orderId));
                            });
                })
                .as(tx::transactional);
    }
    */

    public Mono<Order> deleteOrderItem(String orderId, String itemId) {

        String warehouseId = "31dd5c63-86fa-49a3-ae77-c55f203dec4b";

        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada: " + orderId)))
                .flatMap(order -> {
                    if (!OrderStatus.NEW.equals(order.getStatus())
                            && !OrderStatus.PENDING.equals(order.getStatus())) {
                        return Mono.error(new ConflictException(
                                "La orden no se puede editar en estado: " + order.getStatus()));
                    }

                    return itemRepo.findById(itemId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                            .flatMap(item -> {
                                if (!orderId.equals(item.getOrderId())) {
                                    return Mono.error(new ConflictException("El item no pertenece a la orden"));
                                }

                                // cantidad a liberar
                                BigDecimal releaseQty = item.getQuantity() == null
                                        ? BigDecimal.ZERO
                                        : item.getQuantity();

                                Mono<Void> releaseMono =
                                        (releaseQty.signum() <= 0)
                                                ? Mono.<Void>empty()
                                                : callInventory(new InventoryAdjustmentRequest(
                                                item.getProductId(),
                                                warehouseId,
                                                releaseQty,
                                                "RELEASE",
                                                "Se elimina item de la orden: libera reserva"
                                        ));

                                return releaseMono
                                        .then(itemRepo.deleteById(itemId))        // borrar el ítem
                                        .then(recalculateOrderTotals(orderId));   // recalcular totales
                            });
                })
                .as(tx::transactional);
    }


    // ====== recalcular totales de la orden ======
    private Mono<Order> recalculateOrderTotals(String orderId) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada para recalcular: " + orderId)))
                .flatMap(order ->
                        itemRepo.findByOrderId(orderId)
                                .reduce(BigDecimal.ZERO, (acc, it) -> acc.add(it.getLineTotal()))
                                .flatMap(subtotal -> {
                                    // usa la misma lógica que usaste en el carrito
                                    BigDecimal taxes = subtotal.multiply(new BigDecimal("0.13")); // o traer de config
                                    BigDecimal total = subtotal.add(taxes);
                                    order.setSubtotal(subtotal);
                                    order.setTaxes(taxes);
                                    order.setTotal(total);
                                    order.setUpdatedAt(LocalDateTime.now());
                                    return orderRepo.save(order);
                                })
                );
    }


    private Mono<String> getCustomerEmail(String customerId) {
        return emailClient.get()
                .uri("/api/v1/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerDTO.class)
                .map(CustomerDTO::getEmail)
                .onErrorResume(e -> {
                    System.err.println("Error obteniendo email del cliente: " + e.getMessage());
                    return Mono.just("fallback@example.com");
                });
    }

    public Mono<Order> approve(String orderId, ApproveOrderRequest req) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Orden no encontrada: " + orderId)))
                .flatMap(order -> {
                    if (!(OrderStatus.NEW.equals(order.getStatus()) || OrderStatus.PENDING.equals(order.getStatus()))) {
                        return Mono.error(new ConflictException(
                                "Solo se puede aprobar una orden en estado NEW o PENDING. Estado actual: " + order.getStatus()));
                    }

                    order.setStatus(OrderStatus.APPROVED);
                    order.setUpdatedAt(LocalDateTime.now());

                    return orderRepo.save(order)
                            .flatMap(savedOrder ->
                                    getCustomerEmail(savedOrder.getCustomerId())
                                            .flatMap(email -> {
                                                String subject = "Orden " + savedOrder.getId() + " aprobada";
                                                String body = "Hola, tu orden fue aprobada.\n"
                                                        + "Total: " + savedOrder.getTotal() + " "
                                                        + savedOrder.getCurrency();

                                                return notificationService.sendEmail(email, subject, body)
                                                        .thenReturn(savedOrder);
                                            })
                                            // Si falla el envío del correo, no rompemos la aprobación
                                            .onErrorResume(e -> {
                                                System.err.println("No se pudo enviar correo para la orden " + savedOrder.getId()
                                                        + ": " + e.getMessage());
                                                return Mono.just(savedOrder);
                                            })
                            );
                })
                .as(tx::transactional);
    }

    static class CustomerDTO {
        private String id;
        private String name;
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}

