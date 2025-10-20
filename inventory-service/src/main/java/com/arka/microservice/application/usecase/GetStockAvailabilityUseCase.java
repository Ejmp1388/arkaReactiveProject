package com.arka.microservice.application.usecase;

import com.arka.microservice.application.dto.InventoryAvailabilityResponseDTO;
import com.arka.microservice.application.dto.StockAvailabilityRequestDTO;
import com.arka.microservice.application.dto.ProductRequestDTO;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetStockAvailabilityUseCase {
    private final InventoryRepositoryPort repository;

    public Flux<InventoryAvailabilityResponseDTO> getStockAvailability(StockAvailabilityRequestDTO request){

        List<String> productIds = request.getProductos().stream()
                .map(ProductRequestDTO::getProductId)
                .collect(Collectors.toList());

        return repository.findByProductIdsAndWarehouseId(productIds, request.getWarehouseId())
                .map(inv -> new InventoryAvailabilityResponseDTO(
                        inv.getProductId(),
                        inv.getWarehouseId(),
                        inv.getAvailableQuantity()
                ));
    }

}
