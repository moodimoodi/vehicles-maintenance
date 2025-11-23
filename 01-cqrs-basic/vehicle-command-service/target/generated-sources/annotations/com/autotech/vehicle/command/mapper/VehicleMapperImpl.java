package com.autotech.vehicle.command.mapper;

import com.autotech.vehicle.command.dto.VehicleRegisterRequestDto;
import com.autotech.vehicle.command.dto.VehicleResponseDto;
import com.autotech.vehicle.command.entity.VehicleEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T21:02:36+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class VehicleMapperImpl implements VehicleMapper {

    @Override
    public VehicleEntity toEntity(VehicleRegisterRequestDto request) {
        if ( request == null ) {
            return null;
        }

        VehicleEntity vehicleEntity = new VehicleEntity();

        vehicleEntity.setVin( request.getVin() );
        vehicleEntity.setBrand( request.getBrand() );
        vehicleEntity.setModel( request.getModel() );
        vehicleEntity.setYearOfProduction( request.getYearOfProduction() );
        vehicleEntity.setColor( request.getColor() );
        vehicleEntity.setCurrentMileage( request.getCurrentMileage() );
        vehicleEntity.setCustomerId( request.getCustomerId() );
        vehicleEntity.setRegistrationDate( request.getRegistrationDate() );

        return vehicleEntity;
    }

    @Override
    public VehicleResponseDto toDto(VehicleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        VehicleResponseDto vehicleResponseDto = new VehicleResponseDto();

        vehicleResponseDto.setVin( entity.getVin() );
        vehicleResponseDto.setBrand( entity.getBrand() );
        vehicleResponseDto.setModel( entity.getModel() );
        vehicleResponseDto.setYearOfProduction( entity.getYearOfProduction() );
        vehicleResponseDto.setColor( entity.getColor() );
        vehicleResponseDto.setCurrentMileage( entity.getCurrentMileage() );
        vehicleResponseDto.setCustomerId( entity.getCustomerId() );
        vehicleResponseDto.setRegistrationDate( entity.getRegistrationDate() );

        return vehicleResponseDto;
    }

    @Override
    public void updateEntityFromRequest(VehicleRegisterRequestDto request, VehicleEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setVin( request.getVin() );
        entity.setBrand( request.getBrand() );
        entity.setModel( request.getModel() );
        entity.setYearOfProduction( request.getYearOfProduction() );
        entity.setColor( request.getColor() );
        entity.setCurrentMileage( request.getCurrentMileage() );
        entity.setCustomerId( request.getCustomerId() );
        entity.setRegistrationDate( request.getRegistrationDate() );
    }
}
