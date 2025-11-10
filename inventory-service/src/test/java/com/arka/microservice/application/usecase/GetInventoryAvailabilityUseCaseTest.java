package com.arka.microservice.application.usecase;

import com.arka.microservice.application.dto.InventoryAvailabilityRequestDTO;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetInventoryAvailabilityUseCaseTest {

    @Mock
    private InventoryRepositoryPort repository;

    @InjectMocks
    private GetInventoryAvailabilityUseCase getInventoryAvailabilityUseCase;

    private InventoryAvailabilityRequestDTO requestDTO;
    private InventoryAvailabilityRequestDTO requestDTO2;
    private Inventory inventory1;
    private Inventory inventory2;

    @BeforeEach
    void setUp() {

        requestDTO =new InventoryAvailabilityRequestDTO("prod-1","warehouse-1");
        requestDTO2 =new InventoryAvailabilityRequestDTO("prod-2","warehouse-2");


        // Crear inventarios simulados
        inventory1 = new Inventory();
        inventory1.setProductId("prod-1");
        inventory1.setWarehouseId("warehouse-1");
        inventory1.setAvailableQuantity(50);

        inventory2 = new Inventory();
        inventory2.setProductId("prod-2");
        inventory2.setWarehouseId("warehouse-2");
        inventory2.setAvailableQuantity(20);
    }

    @Test
    void shouldReturnStockAvailabilityForGivenProductsAndWarehouse() {

        // Configurar mock
        when(repository.findByProductAndWarehouseList(List.of("prod-1", "prod-2"), List.of("warehouse-1", "warehouse-2")))
                .thenReturn(Flux.just(inventory1,inventory2));

        Flux<InventoryAvailabilityResponseDTO> result = getInventoryAvailabilityUseCase.getAvailability(List.of(requestDTO,requestDTO2));

        // Verificar resultados
        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getProductId().equals("prod-1") &&
                                dto.getWarehouseId().equals("warehouse-1") &&
                                dto.getAvailableQuantity() == 50
                )
                .expectNextMatches(dto ->
                        dto.getProductId().equals("prod-2") &&
                                dto.getWarehouseId().equals("warehouse-2") &&
                                dto.getAvailableQuantity() == 20
                )
                .verifyComplete();
    }
}
