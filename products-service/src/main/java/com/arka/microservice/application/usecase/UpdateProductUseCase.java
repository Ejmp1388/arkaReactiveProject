package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import com.arka.microservice.domain.validation.ProductValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductUseCase {
    private final ProductRepositoryPort repository;

    public Mono<Product> updateProducto(Product prod) {
        return repository.findById(prod.getId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El id de producto ingresado no existe")))
                .flatMap(existe -> ProductValidator.validate(prod)
                        .flatMap(validated -> {
                            validated.setId(existe.getId());
                            return repository.update(validated);
                        })
                );
    }
}

