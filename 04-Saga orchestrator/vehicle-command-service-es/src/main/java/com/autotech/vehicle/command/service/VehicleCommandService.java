package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.dto.*;

public interface VehicleCommandService {

    VehicleResponseDto register(VehicleRegisterRequestDto request);

    VehicleResponseDto updateDetails(String vin, VehicleUpdateDetailsRequestDto request);

    VehicleResponseDto changeStatus(String vin, VehicleChangeStatusRequestDto request);

    VehicleResponseDto assignOwner(String vin, VehicleAssignOwnerRequestDto request);

    VehicleResponseDto unassignOwner(String vin);
}
