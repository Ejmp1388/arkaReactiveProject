package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import com.arka.microservice.domain.validation.ProductValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateProductUseCase {
    private final ProductRepositoryPort repository;

    public Mono<Product> createProduct(Product productInput) {
        return ProductValidator.validate(productInput)
                .flatMap(repository::save);
    }
}
