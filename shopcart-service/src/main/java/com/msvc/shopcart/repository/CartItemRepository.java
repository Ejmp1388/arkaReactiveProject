package com.msvc.shopcart.repository;


import com.msvc.shopcart.domain.CartItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartItemRepository extends ReactiveCrudRepository<CartItem, String> {
    Flux<CartItem> findByCartId(String cartId);
    Mono<CartItem> findByCartIdAndProductId(String cartId, String productId);
    Mono<Void> deleteByCartIdAndProductId(String cartId, String productId);
    Mono<Void> deleteByCartId(String cartId);
}
