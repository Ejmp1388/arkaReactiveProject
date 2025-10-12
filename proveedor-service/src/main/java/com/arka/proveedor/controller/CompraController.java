package com.arka.proveedor.controller;

import com.arka.proveedor.model.CompraRequest;
import com.arka.proveedor.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/compras")
public class CompraController {
    private final CompraService compraService;

    @PostMapping(value = "/savecompra")
    public Mono<ResponseEntity<Map<String, String>>> registrarCompra(@Valid @RequestBody CompraRequest compraRequest) {
        return compraService.registrarCompra(compraRequest)
                .thenReturn(ResponseEntity.ok(Map.of("mensaje", "La compra ha sido guardada Exitosamente")));
    }
}