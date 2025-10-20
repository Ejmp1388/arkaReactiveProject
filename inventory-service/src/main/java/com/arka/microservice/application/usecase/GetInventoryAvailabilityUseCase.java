package com.arka.microservice.application.usecase;

import com.arka.microservice.application.dto.InventoryAvailabilityRequestDTO;
import com.arka.microservice.application.dto.InventoryAvailabilityResponseDTO;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetInventoryAvailabilityUseCase {
    private final InventoryRepositoryPort repository;

    public Flux<InventoryAvailabilityResponseDTO> getAvailability(List<InventoryAvailabilityRequestDTO> requestList) {

//        List<String> productIds = requestList.stream()
//                .map(InventoryAvailabilityRequestDTO::getProductId)
//                .collect(Collectors.toList());
//
//        List<String> warehouseIds = requestList.stream()
//                .map(InventoryAvailabilityRequestDTO::getWarehouseId)
//                .collect(Collectors.toList());
//
//        return repository.findByProductAndWarehouseList(productIds, warehouseIds)
//                .map(inv -> new InventoryAvailabilityResponseDTO(
//                        inv.getProductId(),
//                        inv.getWarehouseId(),
//                        inv.getAvailableQuantity()
//                ));
//

//-----------------------------------------------------------------------------------------
        // Crear un Set de pares válidos
        Set<String> validPairs = requestList.stream()
                .map(r -> r.getProductId() + "|" + r.getWarehouseId())
                .collect(Collectors.toSet());

        // Traer todos los registros que tengan productId en la lista y warehouseId en la lista (más simple)
        List<String> productIds = requestList.stream()
                .map(InventoryAvailabilityRequestDTO::getProductId)
                .distinct()
                .collect(Collectors.toList());

        List<String> warehouseIds = requestList.stream()
                .map(InventoryAvailabilityRequestDTO::getWarehouseId)
                .distinct()
                .collect(Collectors.toList());

        return repository.findByProductAndWarehouseList(productIds, warehouseIds)
                .filter(inv -> validPairs.contains(inv.getProductId() + "|" + inv.getWarehouseId()))
                .map(inv -> new InventoryAvailabilityResponseDTO(
                        inv.getProductId(),
                        inv.getWarehouseId(),
                        inv.getAvailableQuantity()
                ));
    }
}
