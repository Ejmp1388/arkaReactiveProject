package com.arka.microservice.infrastructure.adapters;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductClientAdapter implements ProductClientPort {

    private final WebClient webClient;

    @Override
    public Mono<Product> getProductById(String id) {
        return webClient
                .get()
                .uri( "http://products-service:8087/api/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .onErrorResume(ex -> {
                    System.err.println("Error al consultar producto: " + ex.getMessage());
                    return Mono.empty();
                });
    }
}
