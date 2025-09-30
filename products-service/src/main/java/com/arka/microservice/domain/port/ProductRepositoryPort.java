package com.arka.microservice.domain.port;

import com.arka.microservice.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {
    Mono<Product> save(Product product);
   // Mono<Product> update(Product product);
    Mono<Product> findById(String id);
    Flux<Product> findAll();
}
