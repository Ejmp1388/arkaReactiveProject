package com.msvc.shopcart.service;


import com.msvc.shopcart.domain.Cart;
import com.msvc.shopcart.domain.CartItem;
import com.msvc.shopcart.dto.InventoryCheckDTO;
import com.msvc.shopcart.dto.InventoryUpdateRequest;
import com.msvc.shopcart.dto.ProductInfo;
import com.msvc.shopcart.dto.ProductResponse;
import com.msvc.shopcart.exception.ConflictException;
import com.msvc.shopcart.exception.NotFoundException;
import com.msvc.shopcart.repositories.CartItemRepository;
import com.msvc.shopcart.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final TransactionalOperator tx;

    private final WebClient productClient;
    private final WebClient customerClient;
    private final WebClient inventoryClient;
    private final WebClient ordersClient;


    /*public CartService(CartRepository cartRepo,
                       CartItemRepository itemRepo,
                       ReactiveTransactionManager rtm) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.tx = TransactionalOperator.create(rtm);
    }
    */


    public CartService(CartRepository cartRepo,
                       CartItemRepository itemRepo,
                       ReactiveTransactionManager rtm,
                       WebClient.Builder webClientBuilder,
                       @Value("${app.customer.base-url}") String customerBaseUrl,
                       @Value("${app.inventory.base-url}") String inventoryBaseUrl,
                       @Value("${app.product.base-url}") String productBaseUrl,
                       @Value("${app.order.base-url}") String orderBaseUrl
    ) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.tx = TransactionalOperator.create(rtm);
        this.customerClient = webClientBuilder.baseUrl(customerBaseUrl).build();
        this.inventoryClient = webClientBuilder.baseUrl(inventoryBaseUrl).build();
        this.productClient = webClientBuilder.baseUrl(productBaseUrl).build();
        this.ordersClient = webClientBuilder.baseUrl(orderBaseUrl).build();
    }


    /*
    public CartService(CartRepository cartRepo,
                       CartItemRepository itemRepo,
                       ReactiveTransactionManager rtm,
                       WebClient.Builder webClientBuilder,
                       @Value("${app.customer.base-url}") String customerBaseUrl,
                       @Value("${app.product.base-url}") String productBaseUrl) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.tx = TransactionalOperator.create(rtm);
        this.customerClient = webClientBuilder.baseUrl(customerBaseUrl).build();
        this.productClient  = webClientBuilder.baseUrl(productBaseUrl).build();   // <- NUEVO
    }
    */

    public Mono<Cart> getActiveCart(String customerId) {
        return cartRepo.findFirstByCustomerIdAndStatus(customerId, "OPEN");
    }


    // === Antes de crear, valida el customer ===
    public Mono<Cart> createCartIfNotExists(String customerId, String currency) {
        return assertCustomerExists(customerId) // <- Validando si customer existe
                .then(getActiveCart(customerId)
                        .switchIfEmpty(Mono.defer(() -> {
                            Cart c = new Cart();
                            c.setCustomerId(customerId);
                            c.setStatus("OPEN");
                            c.setCurrency(currency);
                            c.setSubtotal(BigDecimal.ZERO);
                            c.setTaxes(BigDecimal.ZERO);
                            c.setTotal(BigDecimal.ZERO);
                            return cartRepo.save(c);
                        }))
                );
    }


    public Mono<Cart> getByIdOrThrow(String cartId) {
        return cartRepo.findById(cartId)
                .switchIfEmpty(Mono.error(new NotFoundException("Cart no encontrado: " + cartId)));
    }

    public Flux<CartItem> getItems(String cartId) {
        return itemRepo.findByCartId(cartId);
    }


    public Mono<Cart> addItem(String cartId, CartItem item) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("Solo se pueden agregar items a un cart OPEN"));
                    }

                    return assertProductExists(item.getProductId())
                            .then(assertInventoryAvailable(item.getProductId(), "31dd5c63-86fa-49a3-ae77-c55f203dec4b", item.getQuantity()))
                            .then(Mono.defer(() -> {
                                LocalDateTime now = LocalDateTime.now();
                                item.setCartId(cart.getId());
                                item.setLineTotal(item.getUnitPrice().multiply(item.getQuantity()));
                                item.setCreatedAt(now);
                                item.setUpdatedAt(now);

                                return itemRepo.save(item)
                                        .then(recalculate(cart.getId()));
                            }));
                })
                .as(tx::transactional);
    }
    //Validando Stock
    /*
    public Mono<Cart> addItem(String cartId, CartItem item) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("Solo se pueden agregar items a un cart OPEN"));
                    }
                    if (item.getProductId() == null || item.getProductId().isBlank()) {
                        return Mono.error(new IllegalArgumentException("productId es obligatorio"));
                    }
                    if (item.getQuantity() == null || item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                        return Mono.error(new IllegalArgumentException("quantity debe ser > 0"));
                    }

                    // Validar stock en ms Productos ANTES de insertar
                    return assertStockAvailable(item.getProductId(), item.getQuantity())
                            .then(Mono.defer(() -> {
                                item.setId(null);                  // INSERT
                                item.setCartId(cart.getId());
                                item.setLineTotal(item.getUnitPrice().multiply(item.getQuantity()));
                                return itemRepo.save(item).then(recalculate(cart.getId()));
                            }));
                })
                .as(tx::transactional);
    }
    */

/*
    public Mono<Cart> updateItemQty(String cartId, String itemId, BigDecimal newQty) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("Solo se pueden editar items en un cart OPEN"));
                    }
                    return itemRepo.findById(itemId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                            .flatMap(it -> {
                                if (!cartId.equals(it.getCartId())) {
                                    return Mono.error(new ConflictException("Item no pertenece al cart"));
                                }
                                it.setQuantity(newQty);
                                it.setLineTotal(it.getUnitPrice().multiply(newQty));
                                it.setUpdatedAt(LocalDateTime.now());
                                return itemRepo.save(it);
                            })
                            .then(recalculate(cartId));
                })
                .as(tx::transactional);
    }
    */


    // === validar stock al cambiar cantidad ===
    public Mono<Cart> updateItemQty(String cartId, String itemId, BigDecimal newQty) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("Solo se pueden editar items en un cart OPEN"));
                    }
                    return itemRepo.findById(itemId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                            .flatMap(it -> {
                                if (!cartId.equals(it.getCartId())) {
                                    return Mono.error(new ConflictException("Item no pertenece al cart"));
                                }
                                if (newQty == null || newQty.compareTo(BigDecimal.ZERO) <= 0) {
                                    return Mono.error(new IllegalArgumentException("quantity debe ser > 0"));
                                }

                                return assertInventoryAvailable(it.getProductId(), "31dd5c63-86fa-49a3-ae77-c55f203dec4b", newQty)
                                        .then(Mono.defer(() -> {
                                            it.setQuantity(newQty);
                                            it.setLineTotal(it.getUnitPrice().multiply(newQty));
                                            return itemRepo.save(it).then(recalculate(cartId));
                                        }));
                            });
                })
                .as(tx::transactional);
    }


    public Mono<Cart> removeItem(String cartId, String itemId) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("Solo se pueden eliminar items en un cart OPEN"));
                    }
                    return itemRepo.findById(itemId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado: " + itemId)))
                            .flatMap(it -> {
                                if (!cartId.equals(it.getCartId())) {
                                    return Mono.error(new ConflictException("Item no pertenece al cart"));
                                }
                                return itemRepo.deleteById(itemId);
                            })
                            .then(recalculate(cartId));
                })
                .as(tx::transactional);
    }

    public Mono<Cart> clearCart(String cartId) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("Solo se puede limpiar un cart OPEN"));
                    }
                    return itemRepo.findByCartId(cartId)
                            .flatMap(ci -> itemRepo.deleteById(ci.getId()))
                            .then(recalculate(cartId));
                })
                .as(tx::transactional);
    }

    /*
    public Mono<Cart> confirm(String cartId) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("El cart no está en estado OPEN"));
                    }
                    return recalculate(cartId)
                            .then(reserveAllItemsForCart(cart.getId()))
                            .then(Mono.defer(() -> {
                                cart.setStatus("CONFIRMED");
                                cart.setUpdatedAt(java.time.LocalDateTime.now());
                                return cartRepo.save(cart);
                            }));
                })
                .as(tx::transactional);
    }
    */

    public Mono<Cart> confirm(String cartId) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if (!"OPEN".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("El cart no está en estado OPEN"));
                    }

                    return recalculate(cartId)
                            .then(reserveAllItemsForCart(cart.getId()))
                            .then(Mono.defer(() -> {
                                cart.setStatus("CONFIRMED");
                                cart.setUpdatedAt(java.time.LocalDateTime.now());
                                return cartRepo.save(cart); // <- guarda y emite el Cart confirmado
                            }));
                })
                .as(tx::transactional) // asegura commit de la transacción antes del efecto externo
                // Efecto secundario: crear la orden DESPUÉS del commit.
                .delayUntil(savedCart ->
                        createOrderFromCart(savedCart.getId())
                                // Opcional: NO romper el flujo de confirmación si Orders falla; log y continúa
                                .onErrorResume(e -> {
                                    //log.warn("No se pudo crear la orden para cart {}: {}", savedCart.getId(), e.toString());
                                    return Mono.empty();
                                })
                );
    }


    public Mono<Cart> cancel(String cartId) {
        return getByIdOrThrow(cartId)
                .flatMap(cart -> {
                    if ("CONFIRMED".equals(cart.getStatus())) {
                        return Mono.error(new ConflictException("No se puede cancelar un cart CONFIRMED"));
                    }
                    cart.setStatus("CANCELED");
                    cart.setUpdatedAt(LocalDateTime.now());
                    return cartRepo.save(cart);
                })
                .as(tx::transactional);
    }


    private Mono<Cart> recalculate(String cartId) {
        return cartRepo.findById(cartId)
                .flatMap(cart -> itemRepo.findByCartId(cartId)
                        .reduce(BigDecimal.ZERO, (acc, it) -> acc.add(it.getLineTotal()))
                        .flatMap(subtotal -> {
                            BigDecimal tax = subtotal.multiply(new BigDecimal("0.13")); // IVA ejemplo
                            BigDecimal total = subtotal.add(tax);
                            cart.setSubtotal(subtotal);
                            cart.setTaxes(tax);
                            cart.setTotal(total);
                            cart.setUpdatedAt(LocalDateTime.now());
                            return cartRepo.save(cart);
                        })
                )
                .switchIfEmpty(Mono.error(new NotFoundException("Cart no encontrado: " + cartId)));
    }


    private Mono<Void> assertCustomerExists(String customerId) {
        return customerClient
                .get()
                .uri("/api/v1/customers/{id}", customerId)
                .exchangeToMono((ClientResponse resp) -> {
                    if (resp.statusCode().is2xxSuccessful()) return Mono.empty();
                    if (resp.statusCode().value() == 404)
                        return Mono.error(new NotFoundException("Customer no existe: " + customerId));
                    return resp.createException().flatMap(Mono::error);
                });
    }

    private Mono<Void> assertInventoryAvailable(String productId, String warehouseId,
                                                java.math.BigDecimal requiredQty) {

        return inventoryClient.get()
                .uri("/api/inventory/getInvWithWare/{itemId}/{warehouseId}", productId, warehouseId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)
                                ? Mono.error(new NotFoundException("Inventario no encontrado para producto " + productId +
                                " en almacén " + warehouseId))
                                : resp.createException().flatMap(Mono::error)
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(InventoryCheckDTO.class)
                .flatMap(inv -> {
                    if (inv == null || inv.availableQuantity == null) {
                        return Mono.error(new ConflictException("Respuesta de inventario inválida"));
                    }
                    // Validando si es suficiente
                    if (inv.availableQuantity.compareTo(requiredQty) >= 0) return Mono.empty();
                    return Mono.error(new ConflictException(
                            "Stock insuficiente para producto " + productId +
                                    " en almacén " + warehouseId +
                                    " (disponible " + inv.availableQuantity + ", requerido " + requiredQty + ")"
                    ));
                });
    }


    private Mono<Void> postInventoryReserve(String productId,
                                            String warehouseId,
                                            java.math.BigDecimal qty) {
        InventoryUpdateRequest req = new InventoryUpdateRequest();
        req.productId = productId;
        req.warehouseId = warehouseId;
        req.quantity = qty;
        req.type = "RESERVE";
        req.note = "Se hace reserva de producto";

        return inventoryClient.post()
                .uri("/api/inventory/update")
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.createException().flatMap(Mono::error))
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        resp.createException().flatMap(Mono::error))
                .bodyToMono(Void.class)
                .then();
    }

    private Mono<Void> reserveAllItemsForCart(String cartId) {
        return itemRepo.findByCartId(cartId)
                .flatMap(ci -> {
                    return postInventoryReserve(ci.getProductId(), "31dd5c63-86fa-49a3-ae77-c55f203dec4b", ci.getQuantity());
                })
                .then();
    }


    private Mono<Void> assertProductExists(String productId) {
        return productClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)
                                ? Mono.error(new NotFoundException("Producto no existe: " + productId))
                                : resp.createException().flatMap(Mono::error)
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error))
                .bodyToMono(ProductResponse.class)
                .switchIfEmpty(Mono.error(new ConflictException(
                        "Producto no encontrado o respuesta vacía para ID: " + productId)))
                .flatMap(product -> {
                    if (product.getId() == null || product.getId().isBlank()) {
                        return Mono.error(new ConflictException(
                                "Producto sin ID válido para: " + productId));
                    }
                    return Mono.just(true);
                })
                .then(); // convertimos a Mono<Void>
    }

    private Mono<Void> createOrderFromCart(String cartId) {
        return ordersClient.post()
                .uri("/api/v1/orders/from-cart/{id}", cartId)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    // Idempotencia: si ya existe la orden, se trata como éxito lógico
                    if (resp.statusCode().value() == 409) { // Conflict: orden ya creada
                        return Mono.empty();
                    }
                    return resp.createException().flatMap(Mono::error);
                })
                .timeout(java.time.Duration.ofSeconds(5))
                .then();
    }

    public Flux<Cart> findByStatus(String status) {
        return cartRepo.findByStatus(status);
    }


}
