package com.arka.microservice.domain.port;

import com.arka.microservice.domain.model.Category;
import reactor.core.publisher.Flux;

public interface CategoryRepositoryPort {
    Flux<Category> findAll();
}
