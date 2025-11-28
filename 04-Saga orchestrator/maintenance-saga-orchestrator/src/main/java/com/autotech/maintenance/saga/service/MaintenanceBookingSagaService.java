package com.autotech.maintenance.saga.service;

import com.autotech.maintenance.saga.controller.dto.StartMaintenanceBookingRequestDto;
import com.autotech.maintenance.saga.controller.dto.StartMaintenanceBookingResponseDto;
import com.autotech.maintenance.saga.controller.dto.VehicleReserveCommandPayloadDto;

public interface MaintenanceBookingSagaService {
    StartMaintenanceBookingResponseDto startSaga(StartMaintenanceBookingRequestDto request);

    StartMaintenanceBookingResponseDto reserveVehicle(VehicleReserveCommandPayloadDto request);

    StartMaintenanceBookingResponseDto scheduleMaintenance(StartMaintenanceBookingRequestDto request);

    StartMaintenanceBookingResponseDto cancelMaintnance(StartMaintenanceBookingRequestDto request);
}
