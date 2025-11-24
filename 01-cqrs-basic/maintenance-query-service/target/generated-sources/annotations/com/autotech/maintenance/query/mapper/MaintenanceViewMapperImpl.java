package com.autotech.maintenance.query.mapper;

import com.autotech.maintenance.query.dto.CustomerMaintenanceViewResponseDto;
import com.autotech.maintenance.query.dto.WorkshopDailyPlanningViewResponseDto;
import com.autotech.maintenance.query.entity.CustomerMaintenanceViewEntity;
import com.autotech.maintenance.query.entity.WorkshopDailyPlanningViewEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T21:02:32+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MaintenanceViewMapperImpl implements MaintenanceViewMapper {

    @Override
    public CustomerMaintenanceViewResponseDto toDto(CustomerMaintenanceViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerMaintenanceViewResponseDto customerMaintenanceViewResponseDto = new CustomerMaintenanceViewResponseDto();

        return customerMaintenanceViewResponseDto;
    }

    @Override
    public WorkshopDailyPlanningViewResponseDto toDto(WorkshopDailyPlanningViewEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WorkshopDailyPlanningViewResponseDto workshopDailyPlanningViewResponseDto = new WorkshopDailyPlanningViewResponseDto();

        return workshopDailyPlanningViewResponseDto;
    }
}
