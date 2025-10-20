package com.arka.microservice.infrastructure.config;

import com.arka.microservice.application.usecase.*;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    @Bean
    public CreateInventoryUseCase createInventoryUseCase(InventoryRepositoryPort port){
        return new CreateInventoryUseCase(port);
    }

    @Bean
    public UpdateStockUseCase updateStockUseCase(InventoryRepositoryPort port){
        return new UpdateStockUseCase(port);
    }

    @Bean
    public UpdateStockListUseCase updateStockListUseCase(InventoryRepositoryPort port){
        return new UpdateStockListUseCase(port);
    }

    @Bean
    public GetInventoryUseCase getInventoryUseCase(InventoryRepositoryPort port){
        return new GetInventoryUseCase(port);
    }

    @Bean
    public GetInventoryAvailabilityUseCase getInventoryAvailabilityUseCase(InventoryRepositoryPort port){
        return new GetInventoryAvailabilityUseCase(port);
    }

    @Bean
    public GetStockAvailabilityUseCase getStockAvailabilityUseCase(InventoryRepositoryPort port){
        return new GetStockAvailabilityUseCase(port);
    }
}
