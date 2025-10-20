package com.arka.microservice.infrastructure.postgres.repository;

import com.arka.microservice.domain.model.dto.InventoryWithWarehouseDTO;
import com.arka.microservice.infrastructure.postgres.entity.InventoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InventoryRepository extends R2dbcRepository<InventoryEntity, String> {
    Mono<InventoryEntity> findByProductIdAndWarehouseId(String productId, String warehouseId);
    Flux<InventoryEntity> findByWarehouseId(String warehouseId);

    @Query("""
        SELECT i.inventory_id, i.product_id, i.warehouse_id, i.available_quantity, 
               i.reserved_quantity, i.minimum_quantity,
               w.name AS warehouse_name, w.city AS warehouse_city, 
               w.country AS warehouse_country, w.address AS warehouse_address, w.phone AS warehouse_phone
        FROM inventory i
        JOIN warehouse w ON i.warehouse_id = w.warehouse_id
        WHERE i.product_id = :productId AND i.warehouse_id = :warehouseId
    """)
    Mono<InventoryWithWarehouseDTO> findInventoryWithWarehouse(String productId, String warehouseId);

    //@Query("SELECT * FROM inventory WHERE product_id = ANY(:productIds) AND warehouse_id = ANY(:warehouseIds)")
    @Query("SELECT * FROM inventory WHERE product_id IN (:productIds) AND warehouse_id IN (:warehouseIds)")
    Flux<InventoryEntity> findByProductIdsAndWarehouseIds(List<String> productIds, List<String> warehouseIds);

    @Query("SELECT * FROM inventory WHERE product_id IN (:productIds) AND warehouse_id = :warehouseIds")
    Flux<InventoryEntity> findByProductsAndWarehouse(List<String> productIds, String warehouseIds);

}
