package com.msvc.shopcart.repository;


import com.msvc.shopcart.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
    Mono<Cart> findFirstByCustomerIdAndCheckedOutFalseOrderByCreatedAtDesc(String customerId);
}
