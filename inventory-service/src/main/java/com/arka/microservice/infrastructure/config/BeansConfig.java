package com.arka.microservice.infrastructure.config;

import com.arka.microservice.application.usecase.*;
import com.arka.microservice.domain.port.InventoryRepositoryPort;
import com.arka.microservice.domain.port.ProductClientPort;
import com.arka.microservice.domain.port.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    @Bean
    public CreateInventoryUseCase createInventoryUseCase(InventoryRepositoryPort port, ProductClientPort port2){
        return new CreateInventoryUseCase(port,port2);
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

    @Bean
    public GetWarehouseUseCase getWarehouseUseCase(WarehouseRepositoryPort port){
        return new GetWarehouseUseCase(port);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
