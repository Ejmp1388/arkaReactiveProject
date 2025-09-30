package com.arka.microservice.domain.port;

import com.arka.microservice.domain.model.Brands;
import reactor.core.publisher.Flux;

public interface BrandsRespositoryPort {
    Flux<Brands> findAll();
}
