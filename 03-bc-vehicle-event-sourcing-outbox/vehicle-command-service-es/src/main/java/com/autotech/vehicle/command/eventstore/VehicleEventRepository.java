package com.autotech.vehicle.command.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleEventRepository extends JpaRepository<VehicleEventEntity, Long> {

    List<VehicleEventEntity> findByAggregateIdOrderByVersionAsc(String aggregateId);
}
