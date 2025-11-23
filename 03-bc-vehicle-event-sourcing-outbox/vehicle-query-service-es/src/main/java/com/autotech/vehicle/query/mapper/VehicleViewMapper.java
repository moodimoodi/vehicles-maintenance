package com.autotech.vehicle.query.mapper;

import com.autotech.vehicle.query.dto.VehicleViewResponse;
import com.autotech.vehicle.query.entity.VehicleViewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleViewMapper {

    VehicleViewResponse toDto(VehicleViewEntity entity);
}
