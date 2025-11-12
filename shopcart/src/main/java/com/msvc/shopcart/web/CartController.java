package com.msvc.shopcart.web;


import com.msvc.shopcart.domain.Cart;
import com.msvc.shopcart.domain.CartItem;
import com.msvc.shopcart.dto.*;
import com.msvc.shopcart.mapper.CartMapper;
import com.msvc.shopcart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CartResponse> create(@Valid @RequestBody CreateCartRequest req) {
        return service.createCartIfNotExists(req.getCustomerId(), req.getCurrency())
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @GetMapping("/customer/{customerId}/active")
    public Mono<CartResponse> getActive(@PathVariable String customerId) {
        return service.createCartIfNotExists(customerId, "USD")
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @PostMapping("/{cartId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CartResponse> addItem(@PathVariable String cartId,
                                      @Valid @RequestBody AddItemRequest req) {
        CartItem it = new CartItem();
        it.setProductId(req.getProductId());
        it.setProductName(req.getProductName());
        it.setUnitPrice(req.getUnitPrice());
        it.setQuantity(req.getQuantity());
        it.setCreatedAt(LocalDateTime.now());
        it.setUpdatedAt(LocalDateTime.now());

        return service.addItem(cartId, it)
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @PatchMapping("/{cartId}/items/{itemId}/quantity")
    public Mono<CartResponse> updateQty(@PathVariable String cartId,
                                        @PathVariable String itemId,
                                        @Valid @RequestBody UpdateQtyRequest req) {
        BigDecimal qty = req.getQuantity();
        return service.updateItemQty(cartId, itemId, qty)
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteItem(@PathVariable String cartId, @PathVariable String itemId) {
        return service.removeItem(cartId, itemId).then();
    }

    @DeleteMapping("/{cartId}/items")
    public Mono<CartResponse> clear(@PathVariable String cartId) {
        return service.clearCart(cartId)
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @PostMapping("/{cartId}/confirm")
    public Mono<CartResponse> confirm(@PathVariable String cartId) {
        return service.confirm(cartId)
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @PostMapping("/{cartId}/cancel")
    public Mono<CartResponse> cancel(@PathVariable String cartId) {
        return service.cancel(cartId)
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }

    @GetMapping("/{cartId}")
    public Mono<CartResponse> getById(@PathVariable String cartId) {
        return service.getByIdOrThrow(cartId)
                .flatMap(cart -> service.getItems(cart.getId()).collectList()
                        .map(items -> CartMapper.toCartResponse(cart, items)));
    }


    @GetMapping("/status/{status}")
    public Flux<Cart> getByStatus(@PathVariable String status) {
        return service.findByStatus(status);
    }




}

