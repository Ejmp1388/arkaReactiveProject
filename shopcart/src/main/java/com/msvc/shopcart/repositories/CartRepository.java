package com.msvc.shopcart.repositories;


import com.msvc.shopcart.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
    Mono<Cart> findFirstByCustomerIdAndStatus(String customerId, String status);

    Flux<Cart> findByStatus(String status);
}

