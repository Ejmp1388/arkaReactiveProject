package com.arka.proveedor.controller;

import com.arka.proveedor.entity.CompraDetalle;
import com.arka.proveedor.model.CompraRequest;
import com.arka.proveedor.model.CompraResponse;
import com.arka.proveedor.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/compras")
public class CompraController {
    private final CompraService compraService;

    @PostMapping(value = "/savecompra")
    /*public Mono<ResponseEntity<Map<String, String>>> registrarCompra(@Valid @RequestBody CompraRequest compraRequest) {
    //    return compraService.registrarCompra(compraRequest)
    //            .thenReturn(ResponseEntity.ok(Map.of("mensaje", "La compra ha sido guardada Exitosamente")));
    //}
    */
    public Mono<ResponseEntity<CompraResponse>> registrarCompra(@Valid @RequestBody CompraRequest compraRequest) {
        return compraService.registrarCompra(compraRequest)
                .map(saved -> ResponseEntity.ok(saved));
    }

    @PutMapping(value = "/detail/{id}")
    /*public Mono<ResponseEntity<Map<String, String>>> actualizarDetalle(@PathVariable String id, @RequestBody CompraDetalle compraDetalle){
        return compraService.actualizarDetalle(id, compraDetalle)
                .thenReturn(ResponseEntity.ok(Map.of("mensaje", "Detalle Actualizado Exitosamente")));
    }
    */
    public Mono<ResponseEntity<CompraResponse>> actualizarDetalle(@PathVariable String id, @RequestBody CompraDetalle compraDetalle){
        return compraService.actualizarDetalle(id, compraDetalle)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/details")
    public Mono<ResponseEntity<CompraResponse>> agregarDetalle(@RequestBody CompraDetalle compraDetalle){
        return compraService.agregarDetalle(compraDetalle)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping(value = "/detail/{id}")
    public Mono<ResponseEntity<CompraResponse>> eliminarDetalle(@PathVariable String id){
        return compraService.eliminarDetalle(id)
                .map((ResponseEntity::ok));
    }
}