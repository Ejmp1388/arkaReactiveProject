package com.arka.proveedor.repository;

import com.arka.proveedor.entity.CompraProveedor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CompraProveedorRepository extends ReactiveCrudRepository<CompraProveedor, String> {
}
