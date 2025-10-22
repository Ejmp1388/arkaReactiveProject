package com.arka.microservice.infrastructure.cli;

import com.arka.microservice.application.dto.InventoryAvailabilityRequestDTO;
import com.arka.microservice.application.dto.InventoryAvailabilityResponseDTO;
import com.arka.microservice.application.dto.StockAvailabilityRequestDTO;
import com.arka.microservice.application.dto.StockUpdateRequest;
import com.arka.microservice.application.usecase.*;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.infrastructure.cli.dto.UpdateStockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {


    private final CreateInventoryUseCase createInventoryUseCase;
    private final UpdateStockUseCase updateStockUseCase;
    private final UpdateStockListUseCase updateMultipleStocks;
    private final GetInventoryUseCase getInventoryUseCase;
    private final GetInventoryAvailabilityUseCase getInventoryAvailabilityUseCase;
    private final GetStockAvailabilityUseCase getStockAvailabilityUseCase;

    @PostMapping("/create")
    public Mono<ResponseEntity<Map<String, Object>>> createInventory(@RequestBody Inventory inventory) {
        return createInventoryUseCase.createInventory(inventory)
                .map(i -> {
                    Map<String, Object> body = Map.of(
                            "status", 201,
                            "message", "Inventario Creado correctamente",
                            "inventoryId", i.getInventoryId()
                    );
                    return  ResponseEntity.status(HttpStatus.CREATED).body(body);
                })
                .onErrorResume(ex -> {
                    Map<String, Object> errorBody = Map.of(
                            "status", 400,
                            "error", ex.getMessage()
                    );
                    return Mono.just(ResponseEntity.badRequest().body(errorBody));
                });
    }

    @PostMapping("/update")
    public Mono<ResponseEntity<Map<String, Object>>> updateStock(@RequestBody UpdateStockRequest updateStock) {
        return updateStockUseCase.updateStock(updateStock.getProductId(),
                        updateStock.getWarehouseId(),
                        updateStock.getQuantity(),
                        updateStock.getType(),
                        updateStock.getNote())
                .map(i -> ResponseEntity.ok(Map.of(
                        "status", 200,
                        "message", "Stock actualizado correctamente",
                        "new Stock", i
                )))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "error", ex.getMessage()
                ))));
    }

    @PostMapping("/updateMultipleStock")
    public Flux<Map<String, Object>> updateMultipleStocks(@RequestBody Flux<StockUpdateRequest> requests) {
        return updateMultipleStocks.updateMultipleStocks(requests);
    }

    @GetMapping("/getInvWithWare/{productId}/{warehouseId}")
    public Mono<?> getInventoryWarehouse(@PathVariable String productId, @PathVariable String warehouseId){
        return getInventoryUseCase.inventoryWithWarehouse(productId,warehouseId);
    }

    @GetMapping("/listInventByWarehouse/{warehouseId}")
    public Flux<Inventory> listInventByWareHouse(@PathVariable String warehouseId){
        return getInventoryUseCase.listByWarehouse(warehouseId);
    }

    @PostMapping("/availability")
    public Flux<InventoryAvailabilityResponseDTO> getInventoryAvailability(
            @RequestBody List<InventoryAvailabilityRequestDTO> requestList) {

        return getInventoryAvailabilityUseCase.getAvailability(requestList);
    }

    @PostMapping("/stockAvailability")
    public Flux<InventoryAvailabilityResponseDTO> getStockAvailability(
            @RequestBody StockAvailabilityRequestDTO request) {

        return getStockAvailabilityUseCase.getStockAvailability(request);
    }


}
