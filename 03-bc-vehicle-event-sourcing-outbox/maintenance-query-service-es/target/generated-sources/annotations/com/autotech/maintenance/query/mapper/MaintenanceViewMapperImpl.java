package com.autotech.maintenance.query.mapper;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;
import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-24T20:42:26+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MaintenanceViewMapperImpl implements MaintenanceViewMapper {

    @Override
    public MaintenanceViewResponse toDto(MaintenanceAppointmentViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String appointmentId = null;
        String vehicleVin = null;
        String customerId = null;
        String workshopId = null;
        LocalDateTime scheduledAt = null;
        String maintenanceType = null;
        String status = null;
        LocalDateTime lastUpdatedAt = null;

        appointmentId = entity.getAppointmentId();
        vehicleVin = entity.getVehicleVin();
        customerId = entity.getCustomerId();
        workshopId = entity.getWorkshopId();
        scheduledAt = entity.getScheduledAt();
        maintenanceType = entity.getMaintenanceType();
        status = entity.getStatus();
        lastUpdatedAt = entity.getLastUpdatedAt();

        MaintenanceViewResponse maintenanceViewResponse = new MaintenanceViewResponse( appointmentId, vehicleVin, customerId, workshopId, scheduledAt, maintenanceType, status, lastUpdatedAt );

        return maintenanceViewResponse;
    }
}
