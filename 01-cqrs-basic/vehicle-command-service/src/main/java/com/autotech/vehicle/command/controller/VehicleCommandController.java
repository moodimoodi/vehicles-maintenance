package com.autotech.vehicle.command.controller;

import com.autotech.vehicle.command.dto.VehicleOwnershipChangeRequestDto;
import com.autotech.vehicle.command.dto.VehicleRegisterRequestDto;
import com.autotech.vehicle.command.dto.VehicleResponseDto;
import com.autotech.vehicle.command.dto.VehicleUpdateRequestDto;
import com.autotech.vehicle.command.service.VehicleCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles/commands")
public class VehicleCommandController {

    private final VehicleCommandService service;

    public VehicleCommandController(VehicleCommandService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDto> register(@RequestBody VehicleRegisterRequestDto request) {
        return ResponseEntity.ok(service.registerVehicle(request));
    }

    @PutMapping("/{vin}")
    public ResponseEntity<VehicleResponseDto> update(
            @PathVariable String vin,
            @RequestBody VehicleUpdateRequestDto request) {
        return ResponseEntity.ok(service.updateVehicle(vin, request));
    }

    @PostMapping("/{vin}/ownership")
    public ResponseEntity<VehicleResponseDto> changeOwnership(
            @PathVariable String vin,
            @RequestBody VehicleOwnershipChangeRequestDto request) {
        return ResponseEntity.ok(service.changeOwnership(vin, request));
    }
}
