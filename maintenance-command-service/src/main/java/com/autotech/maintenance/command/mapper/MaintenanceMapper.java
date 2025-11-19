package com.autotech.maintenance.command.mapper;

import com.autotech.maintenance.command.dto.MaintenanceAppointmentRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceAppointmentResponseDto;
import com.autotech.maintenance.command.entity.MaintenanceAppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    @Mapping(target = "appointmentId", ignore = true)
    @Mapping(target = "status", ignore = true)
    MaintenanceAppointmentEntity toEntity(MaintenanceAppointmentRequestDto request);

    MaintenanceAppointmentResponseDto toResponse(MaintenanceAppointmentEntity entity);

    List<MaintenanceAppointmentResponseDto> toResponse(List<MaintenanceAppointmentEntity> entity);
}
