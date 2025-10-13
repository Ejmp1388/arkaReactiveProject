package com.arka.microservice.infrastructure.cli;

import com.arka.microservice.application.usecase.*;
import com.arka.microservice.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ListProductUseCase listProductUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;


    @PostMapping("/create")
    public Mono<ResponseEntity<Map<String, Object>>> create(@RequestBody Product product) {
        return createProductUseCase.createProduct(product)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                "status", HttpStatus.CREATED.value(),
                                "message", "Producto registrado exitosamente",
                                "id", saved.getId()
                        )));
    }

    @PostMapping("/update")
    public Mono<ResponseEntity<Map<String, Object>>> update(@RequestBody Product product) {
        return updateProductUseCase.updateProducto(product)
                .map(saved -> ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "Producto actualizado exitosamente",
                        "id", saved.getId()
                )));
    }

    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable String id) {
        return getProductByIdUseCase.findProductById(id);
    }

    @GetMapping("/showAll")
    public Flux<Product> list() {
        return listProductUseCase.showAll();
    }

    @GetMapping("/allActive")
    public Flux<Product> lista() {
        return listProductUseCase.allActive();
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable String id){
       return  deleteProductUseCase.delete(id);
    }
}
