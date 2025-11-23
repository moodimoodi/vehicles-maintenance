package com.autotech.maintenance.query.mapper;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;
import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaintenanceViewMapper {

    MaintenanceViewResponse toDto(MaintenanceAppointmentViewEntity entity);
}
