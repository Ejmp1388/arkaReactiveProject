package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Inventory;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateInventoryUseCaseTest {
    @Mock
    private InventoryRepositoryPort repository;

    @InjectMocks
    private CreateInventoryUseCase useCase;

    private Inventory validInventory;

    @BeforeEach
    void setup() {
        validInventory = new Inventory();
        validInventory.setInventoryId("1234");
        validInventory.setProductId("68eb4b13dcfbb68ad2575365");
        validInventory.setWarehouseId("31dd5c63-86fa-49a3-ae77-c55f203dec4b");
        validInventory.setAvailableQuantity(100);
        validInventory.setMinimumQuantity(10);
    }

    @Test
    void shouldCreateInventorySuccessfully() {

        when(repository.save(any(Inventory.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        when(repository.saveHistory(any(StockHistory.class)))
                .thenReturn(Mono.empty());


        Mono<Inventory> result = useCase.createInventory(validInventory);


        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getProductId().equals("68eb4b13dcfbb68ad2575365") &&
                                saved.getReservedQuantity() == 0)
                .verifyComplete();

        verify(repository, times(1)).save(any(Inventory.class));
        verify(repository, times(1)).saveHistory(any(StockHistory.class));
    }

    @Test
    void shouldFailWhenProductIdIsNull() {
        validInventory.setProductId(null);

        Mono<Inventory> result = useCase.createInventory(validInventory);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El Id del producto no puede ser nulo o vacío."))
                .verify();
    }//
}
