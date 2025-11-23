package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.dto.*;

public interface VehicleCommandService {

    VehicleResponse register(VehicleRegisterRequest request);

    VehicleResponse updateDetails(String vin, VehicleUpdateDetailsRequest request);

    VehicleResponse changeStatus(String vin, VehicleChangeStatusRequest request);

    VehicleResponse assignOwner(String vin, VehicleAssignOwnerRequest request);

    VehicleResponse unassignOwner(String vin);
}
