package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.model.StockHistory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import com.arka.microservice.domain.port.ProductClientPort;
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

    @Mock
    private ProductClientPort productClientPort;

    @InjectMocks
    private CreateInventoryUseCase useCase;

    private Inventory validInventory;

    private Product product;

    @BeforeEach
    void setup() {
        validInventory = new Inventory();
        validInventory.setInventoryId("123");
        validInventory.setProductId("68eb4b13dcfbb68ad2575365");
        validInventory.setWarehouseId("31dd5c63-86fa-49a3-ae77-c55f203dec4b");
        validInventory.setAvailableQuantity(10);
        validInventory.setMinimumQuantity(5);

        product =new Product();
        product.setId("68eb4b13dcfbb68ad2575365");
    }

    @Test
    void shouldCreateInventorySuccessfully() {
        when(productClientPort.getProductById("68eb4b13dcfbb68ad2575365"))
                .thenReturn(Mono.just(product));

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
    }

    @Test
    void shouldFailWhenWarehouseIdIsNull() {
        validInventory.setWarehouseId(null);

        Mono<Inventory> result = useCase.createInventory(validInventory);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El Id del Almacén no puede ser nulo o vacío."))
                .verify();
    }

    @Test
    void shouldFailWhenAvailableQuantityIsZero() {
        validInventory.setAvailableQuantity(0);


        Mono<Inventory> result = useCase.createInventory(validInventory);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("La cantidad disponible deber ser mayor a 0."))
                .verify();
    }

    @Test
    void shouldFailWhenMinimumQuantityEqualAvailable() {
        validInventory.setAvailableQuantity(10);
        validInventory.setMinimumQuantity(10);

        Mono<Inventory> result = useCase.createInventory(validInventory);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("La cantidad mínima no puede ser mayor o igual a la cantidad disponible"))
                .verify();
    }

    @Test
    void shouldFailWhenMinimumQuantityIsNegative() {
        validInventory.setMinimumQuantity(-10);

        Mono<Inventory> result = useCase.createInventory(validInventory);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("La cantidad mínima no puede ser negativa"))
                .verify();
    }
}
