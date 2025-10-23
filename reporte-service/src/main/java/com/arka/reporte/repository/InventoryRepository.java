package com.arka.reporte.repository;

import com.arka.reporte.dto.LowStockInventoryDTO;
import com.arka.reporte.entity.Inventory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface InventoryRepository extends R2dbcRepository<Inventory, String> {

    @Query("""
            SELECT i.inventory_id, i.product_id, i.warehouse_id, w.name AS warehouse_name,
                    i.available_quantity, i.reserved_quantity, i.minimum_quantity, i.updated_at
            FROM inventory i
            JOIN warehouse w ON i.warehouse_id = w.warehouse_id
            WHERE available_quantity <= minimum_quantity
            """)
    Flux<LowStockInventoryDTO> findLowStock();
}
