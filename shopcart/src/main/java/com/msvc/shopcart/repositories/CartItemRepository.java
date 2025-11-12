package com.msvc.shopcart.repositories;


import com.msvc.shopcart.domain.CartItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CartItemRepository extends ReactiveCrudRepository<CartItem, String> {
    Flux<CartItem> findByCartId(String cartId);
}
