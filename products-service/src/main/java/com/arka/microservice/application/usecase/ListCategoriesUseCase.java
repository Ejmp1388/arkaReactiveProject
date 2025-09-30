package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Category;
import com.arka.microservice.domain.port.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListCategoriesUseCase {
    private final CategoryRepositoryPort repositoryPort;

    public Flux<Category> getCategories(){
        return repositoryPort.findAll();
    }
}
