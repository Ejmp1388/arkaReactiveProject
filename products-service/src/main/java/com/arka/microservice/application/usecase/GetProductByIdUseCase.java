package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetProductByIdUseCase {
    private final ProductRepositoryPort repository;

    public Mono<Product> findProductById(String id) {
        return repository.findById(id);
    }
}
