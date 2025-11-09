package com.arka.microservice.domain.validation;

import com.arka.microservice.domain.model.Product;
import reactor.core.publisher.Mono;

public class ProductValidator {

    public static Mono<Product> validate(Product product) {
        if (product == null) {
            return Mono.error(new IllegalArgumentException("El producto no puede ser nulo"));
        }
        if (isBlank(product.getName())) {
            return Mono.error(new IllegalArgumentException("El nombre del producto es obligatorio"));
        }
        if (isBlank(product.getBrand())) {
            return Mono.error(new IllegalArgumentException("La marca es obligatoria"));
        }
        if (product.getCategories() == null || product.getCategories().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Debe especificar al menos una categoría"));
        }
        if (isBlank(product.getCurrency())) {
            return Mono.error(new IllegalArgumentException("Debe incluir el tipo de moneda"));
        }
        if (!product.getCurrency().equals("USD")) {
            return Mono.error(new IllegalArgumentException("El precio en USD es obligatorio"));
        }
        if (product.getPrice()==null || product.getPrice()<=0) {
            return Mono.error(new IllegalArgumentException("El precio es obligatorio y debe ser mayor a 0"));
        }

        return Mono.just(product);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
