package com.arka.microservice.infrastructure.adapters;

import com.arka.microservice.domain.model.Product;
import com.arka.microservice.domain.port.ProductRepositoryPort;
import com.arka.microservice.infrastructure.mongo.document.ProductDocument;
import com.arka.microservice.infrastructure.mongo.repository.ProductMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductMongoRepository productMongoRepository;

    @Override
    public Mono<Product> save(Product product) {
        return productMongoRepository.save(toDocument(product)).map(this::toDomain);
    }

    @Override
    public Mono<Product> findById(String id) {
        return productMongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return productMongoRepository.findAll().map(this::toDomain);
    }

    private Product toDomain(ProductDocument d) {
        return Product.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .brandId(d.getBrandId())
                .categoryId(d.getCategoryId())
                .images(d.getImages())
                .attributes(d.getAttributes())
                .price(d.getPrice())
                .active(d.isActive())
                .build();
    }

    private ProductDocument toDocument(Product p) {
        ProductDocument d = new ProductDocument();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setDescription(p.getDescription());
        d.setBrandId(p.getBrandId());
        d.setCategoryId(p.getCategoryId());
        d.setImages(p.getImages());
        d.setAttributes(p.getAttributes());
        d.setPrice(p.getPrice());
        d.setActive(p.isActive());
        return d;
    }

}
