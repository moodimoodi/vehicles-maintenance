package com.autotech.vehicle.query.mapper;

import com.autotech.vehicle.query.dto.CustomerVehicleResponseDto;
import com.autotech.vehicle.query.dto.VehicleOverviewResponseDto;
import com.autotech.vehicle.query.entity.CustomerGarageViewEntity;
import com.autotech.vehicle.query.entity.VehicleOverviewViewEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T21:02:38+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class VehicleViewMapperImpl implements VehicleViewMapper {

    @Override
    public VehicleOverviewResponseDto toDto(VehicleOverviewViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        VehicleOverviewResponseDto vehicleOverviewResponseDto = new VehicleOverviewResponseDto();

        return vehicleOverviewResponseDto;
    }

    @Override
    public CustomerVehicleResponseDto toDto(CustomerGarageViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerVehicleResponseDto customerVehicleResponseDto = new CustomerVehicleResponseDto();

        return customerVehicleResponseDto;
    }
}
