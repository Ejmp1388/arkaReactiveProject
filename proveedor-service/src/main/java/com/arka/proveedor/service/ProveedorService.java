package com.arka.proveedor.service;

import com.arka.proveedor.entity.Proveedor;
import com.arka.proveedor.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public Mono<Proveedor> saveProveedor(Proveedor proveedor){
        return proveedorRepository.save(proveedor);
    }

    public Mono<Proveedor> getProveedorById(Long id){
        return proveedorRepository.findById(id);
    }

    public Flux<Proveedor> getProveedoresAll(){
        return proveedorRepository.findAll();
    }

    public Mono<Void> deleteById(Long id){
        return proveedorRepository.deleteById(id);
    }

    public Mono<Proveedor> updateById(Long id, Proveedor proveedor){
        return proveedorRepository.findById(id)
                .flatMap(p -> {
                   p.setName(proveedor.getName());
                   p.setType(proveedor.getType());
                   p.setIdentification(proveedor.getIdentification());
                   p.setAddress(proveedor.getAddress());
                   p.setContact(proveedor.getContact());
                   p.setEmail(proveedor.getEmail());
                   p.setStatus(proveedor.getStatus());
                   return proveedorRepository.save(p);
                });
    }
}
