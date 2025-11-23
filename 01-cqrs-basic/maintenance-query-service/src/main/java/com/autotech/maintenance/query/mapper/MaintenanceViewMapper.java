package com.autotech.maintenance.query.mapper;

import com.autotech.maintenance.query.dto.CustomerMaintenanceViewResponseDto;
import com.autotech.maintenance.query.dto.WorkshopDailyPlanningViewResponseDto;
import com.autotech.maintenance.query.entity.CustomerMaintenanceViewEntity;
import com.autotech.maintenance.query.entity.WorkshopDailyPlanningViewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaintenanceViewMapper {

    CustomerMaintenanceViewResponseDto toDto(CustomerMaintenanceViewEntity entity);

    WorkshopDailyPlanningViewResponseDto toDto(WorkshopDailyPlanningViewEntity entity);
}
