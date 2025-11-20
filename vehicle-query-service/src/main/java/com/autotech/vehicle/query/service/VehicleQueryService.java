package com.autotech.vehicle.query.service;

import com.autotech.vehicle.query.dto.CustomerVehicleResponseDto;
import com.autotech.vehicle.query.dto.VehicleOverviewResponseDto;

import java.util.List;

public interface VehicleQueryService {

    VehicleOverviewResponseDto getVehicleByVin(String vin);

    List<CustomerVehicleResponseDto> getCustomerVehicles(String customerId);
}
