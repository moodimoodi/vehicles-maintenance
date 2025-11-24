package com.autotech.vehicle.query.mapper;

import com.autotech.vehicle.query.dto.VehicleViewResponseDto;
import com.autotech.vehicle.query.entity.VehicleViewEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-24T20:42:13+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class VehicleViewMapperImpl implements VehicleViewMapper {

    @Override
    public VehicleViewResponseDto toDto(VehicleViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        VehicleViewResponseDto vehicleViewResponseDto = new VehicleViewResponseDto();

        vehicleViewResponseDto.setVin( entity.getVin() );
        vehicleViewResponseDto.setBrand( entity.getBrand() );
        vehicleViewResponseDto.setModel( entity.getModel() );
        vehicleViewResponseDto.setYear( entity.getYear() );
        vehicleViewResponseDto.setMileage( entity.getMileage() );
        vehicleViewResponseDto.setStatus( entity.getStatus() );
        vehicleViewResponseDto.setOwnerId( entity.getOwnerId() );
        vehicleViewResponseDto.setLastUpdatedAt( entity.getLastUpdatedAt() );

        return vehicleViewResponseDto;
    }
}
