package com.autotech.vehicle.query.mapper;

import com.autotech.vehicle.query.dto.CustomerVehicleResponseDto;
import com.autotech.vehicle.query.dto.VehicleOverviewResponseDto;
import com.autotech.vehicle.query.entity.CustomerGarageViewEntity;
import com.autotech.vehicle.query.entity.VehicleOverviewViewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleViewMapper {

    VehicleOverviewResponseDto toDto(VehicleOverviewViewEntity entity);

    CustomerVehicleResponseDto toDto(CustomerGarageViewEntity entity);
}
