package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.StockHistory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateInventoryUseCase {
    private final InventoryRepositoryPort repository;

    public Mono<Inventory> createInventory(Inventory inventory) {
        return Mono.defer(() -> validate(inventory))
                .flatMap(this::saveInventoryWithHistory);
    }


    private Mono<Inventory> validate(Inventory inv){
        if (inv.getProductId() == null || inv.getProductId().isBlank()) {
            return Mono.error(new IllegalArgumentException("El Id del producto no puede ser nulo o vacío."));
        }

        if (inv.getWarehouseId() == null || inv.getWarehouseId().isBlank()) {
            return Mono.error(new IllegalArgumentException("El Id del Almacén no puede ser nulo o vacío."));
        }

        if (inv.getAvailableQuantity() <= 0) {
            return Mono.error(new IllegalArgumentException("La cantidad disponible deber ser mayor a 0."));
        }

        if (inv.getMinimumQuantity() >= inv.getAvailableQuantity()) {
            return Mono.error(new IllegalArgumentException("La cantidad mínima no puede ser mayor o igual a la cantidad disponible"));
        }

        if (inv.getMinimumQuantity() < 0) {
            return Mono.error(new IllegalArgumentException("La cantidad mínima no puede ser negativa"));
        }

        inv.setReservedQuantity(0);


        return Mono.just(inv);
    }

    private Mono<Inventory> saveInventoryWithHistory(Inventory inventory) {
        return repository.save(inventory) // guarda inventario
                .flatMap(saved -> {
                    StockHistory history = new StockHistory();
                    history.setInventoryId(saved.getInventoryId());
                    history.setAdjustmentType("CREATED");
                    history.setQuantityBefore(0);
                    history.setQuantityAfter(saved.getAvailableQuantity());
                    history.setNote("Se agrega inventario a nuevo producto");
                    // guarda el historial, luego retorna el inventario guardado
                    return repository.saveHistory(history)
                            .thenReturn(saved);
                });
    }

}
