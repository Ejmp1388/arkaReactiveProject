package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListProductUseCase {
    private final ProductRepositoryPort repository;

    public Flux<Product> allActive() {
        return repository.findAllActive();
    }

    public Flux<Product> showAll() {
        return repository.findAll();
    }
}
