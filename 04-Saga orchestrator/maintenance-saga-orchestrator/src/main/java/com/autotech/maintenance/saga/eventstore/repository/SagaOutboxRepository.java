package com.autotech.maintenance.saga.eventstore.repository;

import com.autotech.maintenance.saga.eventstore.entity.SagaOutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SagaOutboxRepository extends JpaRepository<SagaOutboxEventEntity, Long> {
    List<SagaOutboxEventEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}
