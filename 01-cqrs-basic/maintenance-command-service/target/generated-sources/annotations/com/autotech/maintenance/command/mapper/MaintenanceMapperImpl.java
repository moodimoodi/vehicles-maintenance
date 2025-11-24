package com.autotech.maintenance.command.mapper;

import com.autotech.maintenance.command.dto.MaintenanceAppointmentRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceAppointmentResponseDto;
import com.autotech.maintenance.command.entity.MaintenanceAppointmentEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T21:02:29+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MaintenanceMapperImpl implements MaintenanceMapper {

    @Override
    public MaintenanceAppointmentEntity toEntity(MaintenanceAppointmentRequestDto request) {
        if ( request == null ) {
            return null;
        }

        MaintenanceAppointmentEntity maintenanceAppointmentEntity = new MaintenanceAppointmentEntity();

        maintenanceAppointmentEntity.setVin( request.getVin() );
        maintenanceAppointmentEntity.setCustomerId( request.getCustomerId() );
        maintenanceAppointmentEntity.setWorkshopId( request.getWorkshopId() );
        maintenanceAppointmentEntity.setDate( request.getDate() );
        maintenanceAppointmentEntity.setSlot( request.getSlot() );
        maintenanceAppointmentEntity.setReason( request.getReason() );
        List<String> list = request.getRequestedServices();
        if ( list != null ) {
            maintenanceAppointmentEntity.setRequestedServices( new ArrayList<String>( list ) );
        }

        return maintenanceAppointmentEntity;
    }

    @Override
    public MaintenanceAppointmentResponseDto toResponse(MaintenanceAppointmentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MaintenanceAppointmentResponseDto maintenanceAppointmentResponseDto = new MaintenanceAppointmentResponseDto();

        maintenanceAppointmentResponseDto.setAppointmentId( entity.getAppointmentId() );
        maintenanceAppointmentResponseDto.setVin( entity.getVin() );
        maintenanceAppointmentResponseDto.setCustomerId( entity.getCustomerId() );
        maintenanceAppointmentResponseDto.setWorkshopId( entity.getWorkshopId() );
        maintenanceAppointmentResponseDto.setDate( entity.getDate() );
        maintenanceAppointmentResponseDto.setSlot( entity.getSlot() );
        maintenanceAppointmentResponseDto.setStatus( entity.getStatus() );
        maintenanceAppointmentResponseDto.setReason( entity.getReason() );
        List<String> list = entity.getRequestedServices();
        if ( list != null ) {
            maintenanceAppointmentResponseDto.setRequestedServices( new ArrayList<String>( list ) );
        }

        return maintenanceAppointmentResponseDto;
    }

    @Override
    public List<MaintenanceAppointmentResponseDto> toResponse(List<MaintenanceAppointmentEntity> entity) {
        if ( entity == null ) {
            return null;
        }

        List<MaintenanceAppointmentResponseDto> list = new ArrayList<MaintenanceAppointmentResponseDto>( entity.size() );
        for ( MaintenanceAppointmentEntity maintenanceAppointmentEntity : entity ) {
            list.add( toResponse( maintenanceAppointmentEntity ) );
        }

        return list;
    }
}
