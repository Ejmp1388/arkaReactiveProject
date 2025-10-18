package com.msvc.customer.repository;


import com.msvc.customer.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
    Flux<Customer> findByActiveTrue();
    Flux<Customer> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}

