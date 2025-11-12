package com.msvc.customer.service;

import com.msvc.customer.domain.Customer;
import com.msvc.customer.dto.CreateCustomerRequest;
import com.msvc.customer.dto.UpdateCustomerRequest;
import com.msvc.customer.mapper.CustomerMapper;
import com.msvc.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@Service
@Validated
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public Flux<Customer> listActive(String nameFilter) {
        return (nameFilter == null || nameFilter.isBlank())
                ? repo.findByActiveTrue()
                : repo.findByNameContainingIgnoreCaseAndActiveTrue(nameFilter.trim());
    }

    public Mono<Customer> get(String id) {
        return repo.findById(id);
    }

    public Mono<Customer> create(@Valid CreateCustomerRequest req) {
        return repo.save(CustomerMapper.newEntity(req));
    }

    public Mono<Customer> update(String id, @Valid UpdateCustomerRequest req) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")))
                .map(c -> { CustomerMapper.apply(c, req); return c; })
                .flatMap(repo::save);
    }

    /** Soft delete */
    public Mono<Void> deactivate(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")))
                .flatMap(c -> { c.setActive(false); return repo.save(c); })
                .then();
    }
}

