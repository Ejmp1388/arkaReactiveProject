package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateInventoryUseCase {
    private final InventoryRepositoryPort repository;

    public Mono<Inventory> createInventory(Inventory inventory) {
        return repository.save(inventory);
    }
}
