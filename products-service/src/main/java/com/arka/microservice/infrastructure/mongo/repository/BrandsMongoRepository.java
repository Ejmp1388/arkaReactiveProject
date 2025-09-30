package com.arka.microservice.infrastructure.mongo.repository;

import com.arka.microservice.infrastructure.mongo.document.BrandsDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BrandsMongoRepository  extends ReactiveMongoRepository<BrandsDocument, String> {
}
