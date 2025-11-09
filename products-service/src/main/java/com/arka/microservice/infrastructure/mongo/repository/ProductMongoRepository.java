package com.arka.microservice.infrastructure.mongo.repository;

import com.arka.microservice.infrastructure.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductMongoRepository extends ReactiveMongoRepository<ProductDocument, String> {
    Flux<ProductDocument> findByActiveTrue();
    Mono<Boolean> existsByName(String name);
    Mono<ProductDocument> findByName(String name);
}
