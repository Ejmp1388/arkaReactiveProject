package com.arka.microservice.infrastructure.mongo.repository;

import com.arka.microservice.infrastructure.mongo.document.CategoryDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryMongoRepository  extends ReactiveMongoRepository<CategoryDocument, String> {
}
