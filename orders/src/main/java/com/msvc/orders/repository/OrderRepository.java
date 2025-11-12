package com.msvc.orders.repository;


import com.msvc.orders.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<Order, String> {
    Mono<Boolean> existsByCartId(String cartId);
}
