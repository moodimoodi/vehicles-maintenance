package com.autotech.vehicle.command.eventstore.jpa.repository;

import com.autotech.vehicle.command.eventstore.jpa.entity.VehicleEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleEventRepository extends JpaRepository<VehicleEventEntity, Long> {

    List<VehicleEventEntity> findByAggregateIdOrderByVersionAsc(String aggregateId);
}
