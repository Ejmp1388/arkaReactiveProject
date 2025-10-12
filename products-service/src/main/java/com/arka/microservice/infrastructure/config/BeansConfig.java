package com.arka.microservice.infrastructure.config;

import com.arka.microservice.application.usecase.*;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    @Bean
    public GetProductByIdUseCase getProductByIdUseCase(ProductRepositoryPort port) {
        return new GetProductByIdUseCase(port);
    }

    @Bean
    public ListProductUseCase getAllProducts(ProductRepositoryPort port){
        return new ListProductUseCase(port);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort port){
        return new CreateProductUseCase(port);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepositoryPort port){
        return new UpdateProductUseCase(port);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductRepositoryPort port){
        return new DeleteProductUseCase(port);
    }
}
