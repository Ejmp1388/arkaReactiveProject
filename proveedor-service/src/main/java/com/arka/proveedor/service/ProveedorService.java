package com.arka.proveedor.service;

import com.arka.proveedor.entity.Proveedor;
import com.arka.proveedor.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public Mono<Proveedor> saveProveedor(Proveedor proveedor){
        proveedor.setId(UUID.randomUUID().toString());
        proveedor.setCreatedAt(LocalDateTime.now());
        proveedor.setUpdatedAt(LocalDateTime.now());
        return proveedorRepository.save(proveedor);
    }

    public Mono<Proveedor> getProveedorById(String id){
        return proveedorRepository.findById(id);
    }

    public Flux<Proveedor> getProveedoresAll(){
        return proveedorRepository.findAll();
    }

    public Mono<Void> deleteById(String id){
        return proveedorRepository.deleteById(id);
    }

    public Mono<Proveedor> updateById(String  id, Proveedor proveedor){
        return proveedorRepository.findById(id)
                .flatMap(existing  -> {
                    proveedor.setId(id);
                    proveedor.setCreatedAt(LocalDateTime.now());
                    proveedor.setUpdatedAt(LocalDateTime.now());
                   return proveedorRepository.save(proveedor);
                });
    }


}
