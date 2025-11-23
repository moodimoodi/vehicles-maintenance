package com.autotech.vehicle.query.service;

import com.autotech.vehicle.query.dto.VehicleViewResponse;

import java.util.List;

public interface VehicleQueryService {

    VehicleViewResponse getByVin(String vin);

    List<VehicleViewResponse> getByOwner(String ownerId);

    List<VehicleViewResponse> getByStatus(String status);
}
