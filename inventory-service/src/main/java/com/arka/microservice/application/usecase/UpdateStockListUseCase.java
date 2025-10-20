package com.arka.microservice.application.usecase;

import com.arka.microservice.application.dto.StockUpdateRequest;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.StockHistory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateStockListUseCase {

    private final InventoryRepositoryPort repository;

    public Flux<Map<String, Object>> updateMultipleStocks(Flux<StockUpdateRequest> requests) {
        return requests.flatMap(request -> {//
            return repository.findByProductAndWarehouse(request.getProductId(), request.getWarehouseId())
                    .flatMap(invent -> processStockUpdate(invent, request))
                    .map(invent -> {
                        Map<String, Object> successMap = new HashMap<>();
                        successMap.put("productId", request.getProductId());
                        successMap.put("status", "success");
                        successMap.put("message", "Stock actualizado correctamente");
                        successMap.put("availableQuantity", invent.getAvailableQuantity());
                        successMap.put("reservedQuantity", invent.getReservedQuantity());
                        return successMap;
                    })
                    .onErrorResume(ex -> {
                        Map<String, Object> errorMap = new HashMap<>();
                        errorMap.put("productId", request.getProductId());
                        errorMap.put("status", "error");
                        errorMap.put("message", ex.getMessage());
                        return Mono.just(errorMap);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        Map<String, Object> emptyMap = new HashMap<>();
                        emptyMap.put("productId", request.getProductId());
                        emptyMap.put("status", "error");
                        emptyMap.put("message", "No se encontró inventario para el producto y almacén especificado");
                        return Mono.just(emptyMap);
                    }));
        });
    }

    private Mono<Inventory> processStockUpdate(Inventory invent, StockUpdateRequest request) {

        if (request.getQuantity() <= 0) {
            return Mono.error(new IllegalArgumentException("La cantidad no puede ser 0 o negativo"));
        }


        int beforeAvailable = invent.getAvailableQuantity();
        int beforeReserved = invent.getReservedQuantity();
        int afterAvailable = beforeAvailable;
        int afterReserved = beforeReserved;

        String type = request.getType().toUpperCase();

        switch (type) {
            case "INCREASE":
                afterAvailable = beforeAvailable + request.getQuantity();
                break;
            case "DECREASE":
                if (beforeAvailable < request.getQuantity()) {
                    return Mono.error(new IllegalArgumentException("No hay suficiente stock"));
                }
                afterAvailable = beforeAvailable - request.getQuantity();
                break;
            case "RESERVE":
                if (beforeAvailable < request.getQuantity()) {
                    return Mono.error(new IllegalArgumentException("No hay suficiente stock para la compra"));
                }
                afterAvailable = beforeAvailable - request.getQuantity();
                afterReserved = beforeReserved + request.getQuantity();
                break;
            case "RELEASE":
                if (beforeReserved < request.getQuantity()) {
                    return Mono.error(new IllegalArgumentException("No hay suficiente reserva para liberar"));
                }
                afterAvailable = beforeAvailable + request.getQuantity();
                afterReserved = beforeReserved - request.getQuantity();
                break;
//            case "RELEASER":
//                if (beforeReserved < request.getQuantity()) {
//                    return Mono.error(new IllegalArgumentException("No hay suficiente reserva para liberar"));
//                }
//                afterReserved = beforeReserved - request.getQuantity();
//                break;
            default:
                return Mono.error(new IllegalArgumentException("El tipo es inválido, favor ingrese un valor correcto"));
        }

//El if dependerá de las reglas del negocio, se puede permitir comprar y advertir, o bloquear la compra
//                    if (afterAvailable < invent.getMinimumQuantity()) {
//                        return Mono.error(new IllegalArgumentException("After change stock below minimum"));
//                    }

        invent.setAvailableQuantity(afterAvailable);
        invent.setReservedQuantity(afterReserved);
        invent.setUpdateAt(java.time.LocalDateTime.now());

        StockHistory history = new StockHistory();
        history.setInventoryId(invent.getInventoryId());
        history.setAdjustmentType(type);
        history.setQuantityBefore(beforeAvailable);
        history.setQuantityAfter(afterAvailable);
        history.setNote(request.getNote());

        return repository.save(invent)
                .flatMap(saved -> repository.saveHistory(history).thenReturn(saved));
    }
}