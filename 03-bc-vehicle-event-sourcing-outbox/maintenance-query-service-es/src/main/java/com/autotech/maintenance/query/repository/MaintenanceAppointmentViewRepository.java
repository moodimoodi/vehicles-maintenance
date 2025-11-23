package com.autotech.maintenance.query.repository;

import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceAppointmentViewRepository extends JpaRepository<MaintenanceAppointmentViewEntity, String> {

    List<MaintenanceAppointmentViewEntity> findByCustomerId(String customerId);

    List<MaintenanceAppointmentViewEntity> findByWorkshopId(String workshopId);
}
