package com.arka.microservice.domain.port;

import com.arka.microservice.domain.model.Warehouse;
import reactor.core.publisher.Flux;

public interface WarehouseRepositoryPort {
    Flux<Warehouse> findAll();
}
