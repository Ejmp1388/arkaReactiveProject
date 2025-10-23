package com.arka.reporte.util;

import com.arka.reporte.dto.LowStockInventoryDTO;
import com.arka.reporte.entity.Inventory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvExporter {
    public String generateInventoryCsv(List<LowStockInventoryDTO> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("inventory_id,product_id,warehouse_id,warehouse_name,available_quantity,reservered_quantity,minimum_quantity,updated_at\n");

        for (LowStockInventoryDTO  item : items) {
            sb.append(String.format("%s,%s,%s,%s,%d,%d,%d,%s\n",
                    item.getInventoryId(),
                    item.getProductId(),
                    item.getWarehouseId(),
                    item.getWarehouseName(),
                    item.getAvailableQuantity(),
                    item.getReserveredQuantity(),
                    item.getMinimumQuantity(),
                    item.getUpdatedAt() != null ? item.getUpdatedAt().toString() : ""));
        }

        return sb.toString();
    }
}
