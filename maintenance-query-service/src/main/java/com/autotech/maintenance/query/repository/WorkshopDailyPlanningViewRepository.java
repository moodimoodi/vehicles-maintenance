package com.autotech.maintenance.query.repository;

import com.autotech.maintenance.query.entity.WorkshopDailyPlanningViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkshopDailyPlanningViewRepository extends JpaRepository<WorkshopDailyPlanningViewEntity, String> {

    List<WorkshopDailyPlanningViewEntity> findByWorkshopIdAndDateOrderBySlotAsc(String workshopId, LocalDate date);
}
