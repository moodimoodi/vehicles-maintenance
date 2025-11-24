package com.autotech.maintenance.command.repository;

import com.autotech.maintenance.command.entity.MaintenanceAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceAppointmentRepository extends JpaRepository<MaintenanceAppointmentEntity, String> {

    List<MaintenanceAppointmentEntity> findByCustomerId(String customerId);

    List<MaintenanceAppointmentEntity> findByWorkshopIdAndDate(String workshopId, LocalDate date);
}
