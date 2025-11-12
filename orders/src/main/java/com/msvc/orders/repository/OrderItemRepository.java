package com.msvc.orders.repository;

import com.msvc.orders.domain.OrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, String> {
    Flux<OrderItem> findByOrderId(String orderId);
}
