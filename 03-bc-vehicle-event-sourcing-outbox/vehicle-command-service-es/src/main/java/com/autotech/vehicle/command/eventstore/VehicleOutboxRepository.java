package com.autotech.vehicle.command.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleOutboxRepository extends JpaRepository<VehicleOutboxEventEntity, Long> {

    List<VehicleOutboxEventEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}
