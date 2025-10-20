package com.arka.microservice.infrastructure.adapters;

import com.arka.microservice.domain.model.dto.InventoryWithWarehouseDTO;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.StockHistory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import com.arka.microservice.infrastructure.postgres.entity.InventoryEntity;
import com.arka.microservice.infrastructure.postgres.entity.StockHistoryEntity;
import com.arka.microservice.infrastructure.postgres.repository.InventoryRepository;
import com.arka.microservice.infrastructure.postgres.repository.StockHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepositoryPort {


    private final InventoryRepository inventoryRepository;
    private final StockHistoryRepository historyRepository;

//    public Mono<Inventory> createInventory(Inventory inventory) {
//        InventoryEntity e = toEntity(inventory);
//        return inventoryRepository.save(e).map(this::toDomain);
//    }

    @Override
    public Mono<Inventory> findByProductAndWarehouse(String productId, String warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId,warehouseId).map(this::toDomain);
    }

    @Override
    public Mono<Inventory> save(Inventory inventory) {
        System.out.println("en el adapter antes de guardar "+inventory.getUpdateAt());
        return inventoryRepository.save(toEntity(inventory)).map(this::toDomain);
    }

    @Override
    public Mono<Void> saveHistory(StockHistory history) {
        return historyRepository.save(toHistoryEntity(history)).then();
    }

    @Override
    public Flux<Inventory> findByWarehouse(String warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId).map(this::toDomain);
    }


    @Override
    public Mono<InventoryWithWarehouseDTO> findInventoryWithWarehouse(String productId, String warehouseId) {
        return inventoryRepository.findInventoryWithWarehouse(productId,warehouseId);
    }

    @Override
    public Flux<Inventory> findByProductAndWarehouseList(List<String> productIds, List<String> warehouseIds) {
        return inventoryRepository.findByProductIdsAndWarehouseIds(productIds, warehouseIds)
                .map(this::toDomain);
    }

    @Override
    public Flux<Inventory> findByProductIdsAndWarehouseId(List<String> productIds, String warehouseId) {
        return inventoryRepository.findByProductsAndWarehouse(productIds, warehouseId)
                .map(this::toDomain);
    }

    private InventoryEntity toEntity(Inventory i) {
        InventoryEntity e = new InventoryEntity();
        e.setInventoryId(i.getInventoryId());
        e.setProductId(i.getProductId());
        e.setWarehouseId(i.getWarehouseId());
        e.setAvailableQuantity(i.getAvailableQuantity());
        e.setReservedQuantity(i.getReservedQuantity());
        e.setMinimumQuantity(i.getMinimumQuantity());
        e.setUpdateAt(i.getUpdateAt());
        return e;
    }

    private Inventory toDomain(InventoryEntity e) {
        if (e == null) return null;
        return new Inventory(
                e.getInventoryId(),
                e.getProductId(),
                e.getWarehouseId(),
                e.getAvailableQuantity() != null ? e.getAvailableQuantity() : 0,
                e.getReservedQuantity() != null ? e.getReservedQuantity() : 0,
                e.getMinimumQuantity() != null ? e.getMinimumQuantity() : 0,
                e.getUpdateAt()
        );
    }



    private StockHistoryEntity toHistoryEntity(StockHistory h) {
        StockHistoryEntity e = new StockHistoryEntity();
        e.setHistoryId(h.getHistoryId());
        e.setInventoryId(h.getInventoryId());
        e.setAdjustmentType(h.getAdjustmentType());
        e.setQuantityBefore(h.getQuantityBefore());
        e.setQuantityAfter(h.getQuantityAfter());
        e.setNote(h.getNote());
        return e;
    }
}
