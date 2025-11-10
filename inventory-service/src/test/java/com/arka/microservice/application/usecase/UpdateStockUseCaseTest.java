package com.arka.microservice.application.usecase;

import com.arka.microservice.application.dto.StockUpdateRequest;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.model.StockHistory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateStockUseCaseTest {
    @Mock
    private InventoryRepositoryPort repositoryPort;

    @InjectMocks
    private UpdateStockUseCase updateStockUseCase;

    private StockUpdateRequest stockUpdateRequest;

    private Inventory inventory;

    @BeforeEach
    void setup() {
        stockUpdateRequest = new StockUpdateRequest();
        stockUpdateRequest.setProductId("68eb4b13dcfbb68ad2575365");
        stockUpdateRequest.setWarehouseId("31dd5c63-86fa-49a3-ae77-c55f203dec4b");
        stockUpdateRequest.setQuantity(100);
        stockUpdateRequest.setType("INCREASE");
        stockUpdateRequest.setNote("Incremento unidades");

        inventory=new Inventory();
        inventory.setProductId("68eb4b13dcfbb68ad2575365");
        inventory.setWarehouseId("31dd5c63-86fa-49a3-ae77-c55f203dec4b");
    }

    @Test
    void updateSuccessfully(){

        when(repositoryPort.findByProductAndWarehouse("68eb4b13dcfbb68ad2575365","31dd5c63-86fa-49a3-ae77-c55f203dec4b"))
                .thenReturn(Mono.just(inventory));

        when(repositoryPort.save(any(Inventory.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        when(repositoryPort.saveHistory(any(StockHistory.class)))
                .thenReturn(Mono.empty());

        Mono<Inventory> result=updateStockUseCase.updateStock(stockUpdateRequest.getProductId(),stockUpdateRequest.getWarehouseId(),stockUpdateRequest.getQuantity(),stockUpdateRequest.getType(),stockUpdateRequest.getNote());

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getProductId().equals("68eb4b13dcfbb68ad2575365"))
                .verifyComplete();

        verify(repositoryPort, times(1)).save(any(Inventory.class));
    }


}
