package com.arka.microservice.domain.port;

import com.arka.microservice.domain.model.Product;
import reactor.core.publisher.Mono;

public interface ProductClientPort {
    Mono<Product> getProductById(String id);
}
