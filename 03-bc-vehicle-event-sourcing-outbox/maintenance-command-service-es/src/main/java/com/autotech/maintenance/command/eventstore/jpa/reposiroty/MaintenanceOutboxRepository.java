package com.autotech.maintenance.command.eventstore.jpa.reposiroty;

import com.autotech.maintenance.command.eventstore.jpa.entity.MaintenanceOutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceOutboxRepository extends JpaRepository<MaintenanceOutboxEventEntity, Long> {

    List<MaintenanceOutboxEventEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}
