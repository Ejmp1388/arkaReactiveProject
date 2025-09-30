package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Brands;
import com.arka.microservice.domain.port.BrandsRespositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListBrandsUseCase {
    private final BrandsRespositoryPort repository;

    public Flux<Brands> getBrands() {
        return repository.findAll();
    }
}
