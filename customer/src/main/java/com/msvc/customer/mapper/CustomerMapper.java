package com.msvc.customer.mapper;


import com.msvc.customer.domain.Customer;
import com.msvc.customer.dto.CreateCustomerRequest;
import com.msvc.customer.dto.CustomerResponse;
import com.msvc.customer.dto.UpdateCustomerRequest;

import java.time.Instant;
import java.util.UUID;

public class CustomerMapper {
    private CustomerMapper() {}

    public static Customer newEntity(CreateCustomerRequest r) {
        Customer c = new Customer();
        //c.setId(UUID.randomUUID().toString());
        c.setName(r.getName());
        c.setCountry(r.getCountry());
        c.setState(r.getState());
        c.setDistrict(r.getDistrict());
        c.setCity(r.getCity());
        c.setAddress(r.getAddress());
        c.setPhone(r.getPhone());
        c.setActive(true);
        Instant now = Instant.now();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        c.setEmail(r.getEmail());
        return c;
    }

    public static void apply(Customer c, UpdateCustomerRequest r) {
        c.setName(r.getName());
        c.setCountry(r.getCountry());
        c.setState(r.getState());
        c.setDistrict(r.getDistrict());
        c.setCity(r.getCity());
        c.setAddress(r.getAddress());
        c.setPhone(r.getPhone());
        c.setUpdatedAt(Instant.now());
        c.setEmail(r.getEmail());
    }

    public static CustomerResponse toResponse(Customer c) {
        CustomerResponse dto = new CustomerResponse();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setCountry(c.getCountry());
        dto.setState(c.getState());
        dto.setDistrict(c.getDistrict());
        dto.setCity(c.getCity());
        dto.setAddress(c.getAddress());
        dto.setPhone(c.getPhone());
        dto.setActive(c.isActive());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        dto.setEmail(c.getEmail());
        return dto;
    }
}
