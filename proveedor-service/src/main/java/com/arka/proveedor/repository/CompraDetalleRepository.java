package com.arka.proveedor.repository;

import com.arka.proveedor.entity.CompraDetalle;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CompraDetalleRepository extends ReactiveCrudRepository<CompraDetalle, String> {
}
