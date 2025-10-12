package com.arka.microservice.infrastructure.cli;

import com.arka.microservice.application.usecase.*;
import com.arka.microservice.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Product> create(@RequestBody Product product) {
        return createProductUseCase.createProduct(product);
    }

    @PostMapping("/update")
    public Mono<Product> update(@RequestBody Product product) {
        return updateProductUseCase.updateProducto(product);
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
