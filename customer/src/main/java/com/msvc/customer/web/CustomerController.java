package com.msvc.customer.web;


import com.msvc.customer.dto.CreateCustomerRequest;
import com.msvc.customer.dto.CustomerResponse;
import com.msvc.customer.dto.UpdateCustomerRequest;
import com.msvc.customer.exception.NotFoundException;
import com.msvc.customer.mapper.CustomerMapper;
import com.msvc.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<CustomerResponse> list(@RequestParam(value = "q", required = false) String q) {
        return service.listActive(q).map(CustomerMapper::toResponse);
    }

    /*
    @GetMapping("/{id}")
    public Mono<CustomerResponse> get(@PathVariable String id) {
        return service.get(id).map(CustomerMapper::toResponse);
    }*/

    @GetMapping("/{id}")
    public Mono<CustomerResponse> get(@PathVariable String id) {
        return service.get(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer no encontrado: " + id)))
                .map(CustomerMapper::toResponse);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest req) {
        return service.create(req).map(CustomerMapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<CustomerResponse> update(@PathVariable String id, @Valid @RequestBody UpdateCustomerRequest req) {
        return service.update(id, req).map(CustomerMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deactivate(@PathVariable String id) {
        return service.deactivate(id);
    }
}
