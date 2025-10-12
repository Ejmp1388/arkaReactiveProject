package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteProductUseCase {
    private final ProductRepositoryPort repository;

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
}
