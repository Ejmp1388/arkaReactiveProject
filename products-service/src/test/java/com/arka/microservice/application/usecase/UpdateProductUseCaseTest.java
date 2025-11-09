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

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateProductUseCaseTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @InjectMocks
    private UpdateProductUseCase updateProductUseCase;

    private Product product;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId("690fcae59101aeeede02d57c");
        product.setName("Teclado");
        product.setDescription("Mouse óptico RGB");
        product.setBrand("Logitech");
        product.setCategories(List.of("Periféricos"));
        product.setImages(List.of("img1.jpg", "img2.jpg"));
        product.setAttributes(Map.of("color", "Negro", "dpi", 16000));
        product.setCurrency("USD");
        product.setPrice(20.0);
        product.setActive(true);

        product2 = new Product();
        product2.setId("690fcae59101aeeede02d57d");
        product2.setName("Teclado");
        product2.setDescription("Mouse óptico RGB");
        product2.setBrand("Logitech");
        product2.setCategories(List.of("Periféricos"));
        product2.setImages(List.of("img1.jpg", "img2.jpg"));
        product2.setAttributes(Map.of("color", "Negro", "dpi", 16000));
        product2.setCurrency("USD");
        product2.setPrice(20.0);
        product2.setActive(true);
    }

    @Test
    void updateProductSuccessfully() {
        //actualizar producto correctamente
        when(repositoryPort.findByName("Teclado"))
                .thenReturn(Mono.empty());
        when(repositoryPort.findById("690fcae59101aeeede02d57c"))
                .thenReturn(Mono.just(product));
        when(repositoryPort.update(any(Product.class)))
                .thenReturn(Mono.just(product));
        Mono<Product> result = updateProductUseCase.updateProducto(product);


        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals("690fcae59101aeeede02d57c"))
                .verifyComplete();

        verify(repositoryPort, times(1)).update(any(Product.class));
    }

    @Test
    void updateProductExistAnotherProduct(){
        //Existe producto con otro id pero el mismo nombre
        when(repositoryPort.findByName("Teclado"))
                .thenReturn(Mono.just(product2));
        when(repositoryPort.findById("690fcae59101aeeede02d57c"))
                .thenReturn(Mono.just(product));
        Mono<Product> result = updateProductUseCase.updateProducto(product);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Ya existe otro producto con el mismo nombre."))
                .verify();
        verify(repositoryPort, never()).update(any());
    }
}
