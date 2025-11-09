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
                        .flatMap(validated ->
                                    repository.findByName(validated.getName())
                                            .flatMap(duplicate -> {
                                                // Si existe otro producto con ese nombre y distinto id => error
                                                if (!duplicate.getId().equals(existe.getId())) {
                                                    return Mono.error(new IllegalArgumentException(
                                                            "Ya existe otro producto con el mismo nombre."));
                                                }
                                                validated.setId(existe.getId());
                                                return repository.update(validated);
                                            })
                                            .switchIfEmpty(Mono.defer(() -> {
                                                validated.setId(existe.getId());
                                                return repository.update(validated);
                                            }))
                                ));
    }
}

