package com.autotech.vehicle.query.mapper;

import com.autotech.vehicle.query.dto.VehicleViewResponseDto;
import com.autotech.vehicle.query.entity.VehicleViewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleViewMapper {

    VehicleViewResponseDto toDto(VehicleViewEntity entity);
}
