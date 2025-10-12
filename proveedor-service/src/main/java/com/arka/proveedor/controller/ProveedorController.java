package com.arka.proveedor.controller;

import com.arka.proveedor.entity.Proveedor;
import com.arka.proveedor.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @PostMapping(value = "/save")
    public Mono<Proveedor> saveProveedor(@RequestBody Proveedor proveedor){
        return proveedorService.saveProveedor(proveedor);
    }

    @GetMapping(value = "/{id}")
    public Mono<Proveedor> getProveedorById(@PathVariable("id") Long id){
        return proveedorService.getProveedorById(id);
    }

    @GetMapping(value = "/getall", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Proveedor> getProveedoresAll(){
        return proveedorService.getProveedoresAll();
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> deleteById(@PathVariable("id") Long id){
        return proveedorService.deleteById(id);
    }

    @PutMapping(value = "/{id}")
    public Mono<Proveedor> updateById(@PathVariable("id") Long id, @RequestBody Proveedor proveedor){
        return proveedorService.updateById(id, proveedor);
    }
}