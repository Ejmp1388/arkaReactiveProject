package com.arka.reporte.ventas.repository;

import com.arka.reporte.ventas.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
    Flux<Customer> findByIdIn(List<String> customerIds);
}
