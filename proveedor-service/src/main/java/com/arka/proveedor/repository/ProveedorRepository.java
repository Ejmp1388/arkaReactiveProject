package com.arka.proveedor.repository;

import com.arka.proveedor.entity.Proveedor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface ProveedorRepository extends ReactiveCrudRepository<Proveedor, String> {
}
