package com.arka.reporte.service;

import com.arka.reporte.dto.LowStockInventoryDTO;
import com.arka.reporte.entity.Inventory;
import com.arka.reporte.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    public Flux<LowStockInventoryDTO> getLowStockItems() {
        return repository.findLowStock();
    }
}
