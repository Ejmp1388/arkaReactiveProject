package com.arka.microservice.application.usecase;

import com.arka.microservice.application.dto.InventoryAvailabilityResponseDTO;
import com.arka.microservice.application.dto.ProductRequestDTO;
import com.arka.microservice.application.dto.StockAvailabilityRequestDTO;
import com.arka.microservice.domain.model.Inventory;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetStockAvailabilityUseCaseTest {
    @Mock
    private InventoryRepositoryPort repository;

    @InjectMocks
    private GetStockAvailabilityUseCase useCase;

    private StockAvailabilityRequestDTO request;
    private Inventory inventory1;
    private Inventory inventory2;

    @BeforeEach
    void setUp() {

        // Crear DTO de entrada
        ProductRequestDTO prod1 = new ProductRequestDTO("prod-1");
        ProductRequestDTO prod2 = new ProductRequestDTO("prod-2");
        request = new StockAvailabilityRequestDTO("warehouse-1", List.of(prod1, prod2));

        // Crear inventarios simulados
        inventory1 = new Inventory();
        inventory1.setProductId("prod-1");
        inventory1.setWarehouseId("warehouse-1");
        inventory1.setAvailableQuantity(50);

        inventory2 = new Inventory();
        inventory2.setProductId("prod-2");
        inventory2.setWarehouseId("warehouse-1");
        inventory2.setAvailableQuantity(20);

    }

    @Test
    void shouldReturnStockAvailabilityForGivenProductsAndWarehouse() {

        // Configurar mock
        when(repository.findByProductIdsAndWarehouseId(List.of("prod-1", "prod-2"), "warehouse-1"))
                .thenReturn(Flux.just(inventory1,inventory2));

        // Ejecutar caso de uso
        Flux<InventoryAvailabilityResponseDTO> result = useCase.getStockAvailability(request);

        // Verificar resultados
        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getProductId().equals("prod-1") &&
                                dto.getWarehouseId().equals("warehouse-1") &&
                                dto.getAvailableQuantity() == 50
                )
                .expectNextMatches(dto ->
                        dto.getProductId().equals("prod-2") &&
                                dto.getWarehouseId().equals("warehouse-1") &&
                                dto.getAvailableQuantity() == 20
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoInventoriesFound() {
        // Configurar mock vacío
        when(repository.findByProductIdsAndWarehouseId(anyList(), anyString()))
                .thenReturn(Flux.empty());

        Flux<InventoryAvailabilityResponseDTO> result = useCase.getStockAvailability(request);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
