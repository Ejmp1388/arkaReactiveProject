package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.StockHistory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateStockUseCase {

    private final InventoryRepositoryPort repository;

    public Mono<Inventory> updateStock(String productId, String warehouseId, int quantity, String type, String note) {
        if (quantity <= 0) {
            return Mono.error(new IllegalArgumentException("La cantidad no puede ser 0 o negativo"));
        }
        return repository.findByProductAndWarehouse(productId, warehouseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró inventario para el producto y almacén especificado")))
                .flatMap(invent -> {
                    int beforeAvailable = invent.getAvailableQuantity();
                    int beforeReserved = invent.getReservedQuantity();
                    int afterAvailable = beforeAvailable;
                    int afterReserved = beforeReserved;

                    switch (type.toUpperCase()) {
                        case "INCREASE":
                            afterAvailable = beforeAvailable + quantity;
                            break;
                        case "DECREASE":
                            if (beforeAvailable < quantity) {
                                return Mono.error(new IllegalArgumentException("No hay suficiente stock"));
                            }
                            afterAvailable = beforeAvailable - quantity;
                            break;
                        case "RESERVE":
                            if (beforeAvailable < quantity) {
                                return Mono.error(new IllegalArgumentException("No hay suficiente stock para la compra"));
                            }
                            afterAvailable = beforeAvailable - quantity;
                            afterReserved = beforeReserved + quantity;
                            break;
                        case "RELEASE":
                            if (beforeReserved < quantity) {
                                return Mono.error(new IllegalArgumentException("No hay suficiente reserva para liberar"));
                            }
                            afterAvailable = beforeAvailable + quantity;
                            afterReserved = beforeReserved - quantity;
                            break;
                        case "OUTRESERVE":
                            if (beforeReserved < quantity) {
                                return Mono.error(new IllegalArgumentException("No hay suficiente reserva para liberar"));
                            }
                            afterReserved = beforeReserved - quantity;
                            break;
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



                    System.out.println("fecha en el caso de usao: "+invent.getUpdateAt());

                    StockHistory history = new StockHistory();
                    history.setInventoryId(invent.getInventoryId());
                    history.setAdjustmentType(type.toUpperCase());
                    history.setQuantityBefore(beforeAvailable);
                    history.setQuantityAfter(afterAvailable);
                    history.setNote(note);

                    return repository.save(invent)
                            .flatMap(saved -> repository.saveHistory(history).thenReturn(saved));
                });
    }
}

