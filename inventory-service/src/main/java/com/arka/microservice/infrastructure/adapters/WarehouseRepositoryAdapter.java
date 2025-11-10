package com.arka.microservice.infrastructure.adapters;

import com.arka.microservice.domain.model.Warehouse;
import com.arka.microservice.domain.port.WarehouseRepositoryPort;
import com.arka.microservice.infrastructure.postgres.entity.WarehouseEntity;
import com.arka.microservice.infrastructure.postgres.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class WarehouseRepositoryAdapter implements WarehouseRepositoryPort {
    public final WarehouseRepository warehouseRepository;

    @Override
    public Flux<Warehouse> findAll() {
        return warehouseRepository.findAll().map(this::toDomain);
    }

    private Warehouse toDomain(WarehouseEntity warehouseEntity){
        if (warehouseEntity == null) return null;
        return new Warehouse(
                warehouseEntity.getWarehouseId(),
                warehouseEntity.getName(),
                warehouseEntity.getCity(),
                warehouseEntity.getCountry(),
                warehouseEntity.getAddress(),
                warehouseEntity.getPhone(),
                warehouseEntity.getCreatedAt()
        );
    }
}
