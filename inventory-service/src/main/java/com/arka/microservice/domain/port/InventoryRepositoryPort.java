package com.arka.microservice.domain.port;

import com.arka.microservice.domain.model.dto.InventoryWithWarehouseDTO;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.StockHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface InventoryRepositoryPort {
   // Mono<Inventory> createInventory(Inventory inventory);
    Mono<Inventory> findByProductAndWarehouse(String productId, String warehouseId);
    Mono<Inventory> save(Inventory inventory);
    Mono<Void> saveHistory(StockHistory history);
    Flux<Inventory> findByWarehouse(String warehouseId);
    Mono<InventoryWithWarehouseDTO> findInventoryWithWarehouse(String productId, String warehouseId);
    Flux<Inventory> findByProductAndWarehouseList(List<String> productIds, List<String> warehouseIds);
    Flux<Inventory> findByProductIdsAndWarehouseId(List<String> productIds, String warehouseId);


}
