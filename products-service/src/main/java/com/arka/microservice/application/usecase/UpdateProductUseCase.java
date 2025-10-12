package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductUseCase {
    private final ProductRepositoryPort repository;

   /* public Mono<Product> updateProducto(Product prod) {
        return repository.findById(prod.getId())
                .flatMap(existe->{
                    prod.setId(existe.getId());
                    return repository.update(prod);
                });
    }*/
}

