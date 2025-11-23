package com.autotech.maintenance.command.eventstore.jpa.reposiroty;

import com.autotech.maintenance.command.eventstore.jpa.entity.MaintenanceEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceEventRepository extends JpaRepository<MaintenanceEventEntity, Long> {

    List<MaintenanceEventEntity> findByAggregateIdOrderByVersionAsc(String aggregateId);
}
