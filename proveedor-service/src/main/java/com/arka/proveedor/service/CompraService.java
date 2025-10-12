package com.arka.proveedor.service;

import com.arka.proveedor.entity.CompraDetalle;
import com.arka.proveedor.model.CompraRequest;
import com.arka.proveedor.repository.CompraDetalleRepository;
import com.arka.proveedor.repository.CompraProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraProveedorRepository compraProveedorRepository;
    private final CompraDetalleRepository compraDetalleRepository;

    public Mono<Void> registrarCompra(CompraRequest request) {
        return compraProveedorRepository.save(request.getCompraProveedor())
                .flatMap(compra -> {
                    List<CompraDetalle> detalles = request.getCompraDetalles().stream()
                            .peek(det -> det.setIdPurcharse(compra.getId()))
                            .collect(Collectors.toList());
                    return compraDetalleRepository.saveAll(detalles).then();
                });
    }
}
