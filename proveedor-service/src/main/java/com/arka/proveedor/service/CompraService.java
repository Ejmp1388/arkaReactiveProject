package com.arka.proveedor.service;

import com.arka.proveedor.dto.InventoryUpdateRequest;
import com.arka.proveedor.entity.CompraDetalle;
import com.arka.proveedor.entity.CompraProveedor;
import com.arka.proveedor.model.CompraRequest;
import com.arka.proveedor.model.CompraResponse;
import com.arka.proveedor.repository.CompraDetalleRepository;
import com.arka.proveedor.repository.CompraProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {
    private static final Logger log = LoggerFactory.getLogger(CompraService.class);

    private final CompraProveedorRepository compraProveedorRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final WebClient.Builder webClientBuilder;

    public Mono<CompraResponse> registrarCompra(CompraRequest request) {
        CompraProveedor compraProveedor = request.getCompraProveedor();
        compraProveedor.setCreatedAt(LocalDateTime.now());
        compraProveedor.setUpdatedAt(LocalDateTime.now());
        compraProveedor.setTotal(BigDecimal.ZERO); //Temporal

        return compraProveedorRepository.save(compraProveedor)
                .flatMap(compra -> {
                    List<CompraDetalle> detalles = request.getCompraDetalles().stream()
                            .peek(det -> det.setPurchaseId(compra.getId()))
                            .collect(Collectors.toList());
                    //return compraDetalleRepository.saveAll(detalles).then();
                    return compraDetalleRepository.saveAll(detalles)
                            .collectList()
                            .flatMap(savedDetalles -> {
                                BigDecimal total = savedDetalles.stream()
                                        .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                compra.setTotal(total);
                                compra.setUpdatedAt(LocalDateTime.now());

                                WebClient webClient = webClientBuilder
                                        .baseUrl("http://alb-if-arka-1265771644.us-east-1.elb.amazonaws.com")
                                        .build();

                                // Llamadas al endpoint de inventory por cada detalle; errores no bloquean el flujo
                                return Flux.fromIterable(savedDetalles)
                                        .flatMap(det -> {
                                            InventoryUpdateRequest req = new InventoryUpdateRequest(
                                                    det.getProductId(),
                                                    "31dd5c63-86fa-49a3-ae77-c55f203dec4b",
                                                    det.getQuantity(),
                                                    "INCREASE",
                                                    "Incremento " + det.getQuantity() + " mas"
                                            );

                                            return webClient.post()
                                                    .uri("/api/inventory/update")
                                                    .bodyValue(req)
                                                    .retrieve()
                                                    .bodyToMono(Void.class)
                                                    .doOnError(e -> log.warn("Error actualizando inventario para productId {}: {}", det.getProductId(), e.getMessage()))
                                                    .onErrorResume(e -> Mono.empty());
                                        })

                                        .then(compraProveedorRepository.save(compra)
                                        .map(s -> {
                                            CompraResponse response = new CompraResponse();
                                            response.setCompraProveedor(s);
                                            response.setCompraDetalles(savedDetalles);
                                            return response;
                                        }));
                            });


                });
    }

    public Mono<CompraResponse> actualizarDetalle(String id, CompraDetalle actDetalle){
        return compraDetalleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado")))
                .flatMap(detalleExistente -> {
                    detalleExistente.setProductId(actDetalle.getProductId());
                    detalleExistente.setQuantity(actDetalle.getQuantity());
                    detalleExistente.setUnitPrice(actDetalle.getUnitPrice());
                   return compraDetalleRepository.save(detalleExistente);
                })
                .flatMap(detalleActualizado ->{
                    String purchaseId = detalleActualizado.getPurchaseId();

                    return compraDetalleRepository.findAll()
                            .filter(d -> d.getPurchaseId().equals(purchaseId))
                            .collectList()
                            .flatMap(detallesActualizado ->{
                                BigDecimal nuevoTotal = detallesActualizado.stream()
                                        .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                return compraProveedorRepository.findById(purchaseId)
                                        .flatMap(compra -> {
                                            compra.setTotal(nuevoTotal);
                                            compra.setUpdatedAt(LocalDateTime.now());

                                            return compraProveedorRepository.save(compra)
                                                    .map(s -> {
                                                        CompraResponse response = new CompraResponse();
                                                        response.setCompraProveedor(s);
                                                        response.setCompraDetalles(detallesActualizado);
                                                        return response;
                                                    });
                                        });
                            });
                });
    }

    public Mono<CompraResponse> agregarDetalle(CompraDetalle nuevoDetalle){
        return compraDetalleRepository.save(nuevoDetalle)
                .flatMap(detalleGuardado -> {
                    String purchaseId = detalleGuardado.getPurchaseId();

                    return compraDetalleRepository.findAll()
                            .filter(d -> d.getPurchaseId().equals(purchaseId))
                            .collectList()
                            .flatMap(detallesActualizado -> {
                                BigDecimal nuevoTotal = detallesActualizado.stream()
                                        .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                return compraProveedorRepository.findById(purchaseId)
                                        .flatMap(compra -> {
                                           compra.setTotal(nuevoTotal);
                                           compra.setUpdatedAt(LocalDateTime.now());

                                           return compraProveedorRepository.save(compra)
                                                   .map(s -> {
                                                       CompraResponse response = new CompraResponse();
                                                       response.setCompraProveedor(s);
                                                       response.setCompraDetalles(detallesActualizado);
                                                       return response;
                                                   });
                                        });
                            });
                });
    }

    public Mono<CompraResponse> eliminarDetalle(String id){
        return compraDetalleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado")))
                .flatMap(detalleEliminado -> {
                    String purchaseId = detalleEliminado.getPurchaseId();

                    return compraDetalleRepository.deleteById(id)
                            .then(compraDetalleRepository.findAll().filter(d -> d.getPurchaseId().equals(purchaseId)).collectList())
                            .flatMap(detallesRestantes ->{
                                BigDecimal nuevoTotal = detallesRestantes.stream()
                                        .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                return compraProveedorRepository.findById(purchaseId)
                                        .flatMap(compra ->{
                                            compra.setTotal(nuevoTotal);
                                            compra.setUpdatedAt(LocalDateTime.now());

                                            return compraProveedorRepository.save(compra)
                                                    .map(s -> {
                                                        CompraResponse response = new CompraResponse();
                                                        response.setCompraProveedor(s);
                                                        response.setCompraDetalles(detallesRestantes);
                                                        return response;
                                                    });

                                        });
                            });

                });
    }
}
