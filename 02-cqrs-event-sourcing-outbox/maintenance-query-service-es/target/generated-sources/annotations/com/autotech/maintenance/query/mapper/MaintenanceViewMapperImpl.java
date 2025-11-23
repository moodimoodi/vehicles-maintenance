package com.autotech.maintenance.query.mapper;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;
import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T21:38:27+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MaintenanceViewMapperImpl implements MaintenanceViewMapper {

    @Override
    public MaintenanceViewResponse toDto(MaintenanceAppointmentViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MaintenanceViewResponse maintenanceViewResponse = new MaintenanceViewResponse();

        maintenanceViewResponse.setAppointmentId( entity.getAppointmentId() );
        maintenanceViewResponse.setVehicleVin( entity.getVehicleVin() );
        maintenanceViewResponse.setCustomerId( entity.getCustomerId() );
        maintenanceViewResponse.setWorkshopId( entity.getWorkshopId() );
        maintenanceViewResponse.setScheduledAt( entity.getScheduledAt() );
        maintenanceViewResponse.setMaintenanceType( entity.getMaintenanceType() );
        maintenanceViewResponse.setStatus( entity.getStatus() );
        maintenanceViewResponse.setLastUpdatedAt( entity.getLastUpdatedAt() );

        return maintenanceViewResponse;
    }
}
