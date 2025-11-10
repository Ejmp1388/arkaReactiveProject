package com.arka.microservice.infrastructure.postgres.repository;

import com.arka.microservice.infrastructure.postgres.entity.WarehouseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WarehouseRepository extends R2dbcRepository<WarehouseEntity,String> {
}
