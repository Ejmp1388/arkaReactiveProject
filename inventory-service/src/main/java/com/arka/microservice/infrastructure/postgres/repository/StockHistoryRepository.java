package com.arka.microservice.infrastructure.postgres.repository;

import com.arka.microservice.infrastructure.postgres.entity.StockHistoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface StockHistoryRepository  extends R2dbcRepository<StockHistoryEntity, String> {
}
