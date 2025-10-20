package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.dto.InventoryWithWarehouseDTO;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class GetInventoryUseCase {
    private final InventoryRepositoryPort repository;

    public GetInventoryUseCase(InventoryRepositoryPort repository) {
        this.repository = repository;
    }

    public Flux<Inventory> listByWarehouse(String warehouseId) {
        return repository.findByWarehouse(warehouseId);
    }

    public Mono<InventoryWithWarehouseDTO> inventoryWithWarehouse(String productId, String warehouseId){
        return repository.findInventoryWithWarehouse(productId,warehouseId);
    }
}
