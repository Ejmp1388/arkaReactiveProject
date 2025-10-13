package com.arka.microservice.application.usecase;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

public class CreateProductUseCaseTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setName("Mouse Mejorado");
        product.setDescription("Mouse óptico RGB");
        product.setBrand("Logitech");
        product.setCategories(List.of("Periféricos"));
        product.setImages(List.of("img1.jpg", "img2.jpg"));
        product.setAttributes(Map.of("color", "Negro", "dpi", 16000));
        product.setPrice(Map.of("COP", 80000.0, "USD", 20.0));
        product.setActive(true);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        when(repositoryPort.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> result = createProductUseCase.createProduct(product);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Mouse Mejorado"))
                .verifyComplete();

        verify(repositoryPort, times(1)).save(any(Product.class));
    }
}
