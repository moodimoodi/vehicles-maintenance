package com.autotech.vehicle.command.mapper;

import com.autotech.vehicle.command.dto.VehicleRegisterRequestDto;
import com.autotech.vehicle.command.dto.VehicleResponseDto;
import com.autotech.vehicle.command.entity.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleEntity toEntity(VehicleRegisterRequestDto request);

    VehicleResponseDto toDto(VehicleEntity entity);

    void updateEntityFromRequest(VehicleRegisterRequestDto request, @MappingTarget VehicleEntity entity);
}
