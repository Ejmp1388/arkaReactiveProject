package com.arka.microservice.infrastructure.mongo.repository;

import com.arka.microservice.infrastructure.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductMongoRepository extends ReactiveMongoRepository<ProductDocument, String> {
    Flux<ProductDocument> findByActiveTrue();
}
