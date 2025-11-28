package com.autotech.vehicle.query.service;

import com.autotech.vehicle.query.dto.VehicleViewResponseDto;

import java.util.List;

public interface VehicleQueryService {

    VehicleViewResponseDto getByVin(String vin);

    List<VehicleViewResponseDto> getByOwner(String ownerId);

    List<VehicleViewResponseDto> getByStatus(String status);
}
