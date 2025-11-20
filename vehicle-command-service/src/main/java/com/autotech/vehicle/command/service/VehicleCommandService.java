package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.dto.VehicleOwnershipChangeRequestDto;
import com.autotech.vehicle.command.dto.VehicleRegisterRequestDto;
import com.autotech.vehicle.command.dto.VehicleResponseDto;
import com.autotech.vehicle.command.dto.VehicleUpdateRequestDto;

public interface VehicleCommandService {

    VehicleResponseDto registerVehicle(VehicleRegisterRequestDto request);

    VehicleResponseDto updateVehicle(String vin, VehicleUpdateRequestDto request);

    VehicleResponseDto changeOwnership(String vin, VehicleOwnershipChangeRequestDto request);
}
