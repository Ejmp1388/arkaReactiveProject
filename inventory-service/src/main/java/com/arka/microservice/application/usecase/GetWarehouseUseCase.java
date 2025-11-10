package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Warehouse;
import com.arka.microservice.domain.port.WarehouseRepositoryPort;
import reactor.core.publisher.Flux;

public class GetWarehouseUseCase {
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public GetWarehouseUseCase(WarehouseRepositoryPort warehouseRepositoryPort) {
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public Flux<Warehouse> getWarehouses(){
        return warehouseRepositoryPort.findAll();
    }
}
