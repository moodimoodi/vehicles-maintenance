package com.autotech.maintenance.saga.eventstore.repository;

import com.autotech.maintenance.saga.eventstore.entity.SagaEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SagaEventRepository extends JpaRepository<SagaEventEntity, Long> {
    List<SagaEventEntity> findBySagaIdOrderByVersionAsc(String sagaId);
}
