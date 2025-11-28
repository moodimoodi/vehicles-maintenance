package com.autotech.vehicle.command.controller;

import com.autotech.vehicle.command.dto.*;
import com.autotech.vehicle.command.service.VehicleCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles/commands")
@RequiredArgsConstructor
public class VehicleCommandController {

    private final VehicleCommandService service;

    @PostMapping
    public ResponseEntity<VehicleResponseDto> register(@RequestBody VehicleRegisterRequestDto request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/{vin}/details")
    public ResponseEntity<VehicleResponseDto> updateDetails(
            @PathVariable String vin,
            @RequestBody VehicleUpdateDetailsRequestDto request) {
        return ResponseEntity.ok(service.updateDetails(vin, request));
    }

    @PostMapping("/{vin}/status")
    public ResponseEntity<VehicleResponseDto> changeStatus(
            @PathVariable String vin,
            @RequestBody VehicleChangeStatusRequestDto request) {
        return ResponseEntity.ok(service.changeStatus(vin, request));
    }

    @PostMapping("/{vin}/owner")
    public ResponseEntity<VehicleResponseDto> assignOwner(
            @PathVariable String vin,
            @RequestBody VehicleAssignOwnerRequestDto request) {
        return ResponseEntity.ok(service.assignOwner(vin, request));
    }

    @PostMapping("/{vin}/owner/unassign")
    public ResponseEntity<VehicleResponseDto> unassignOwner(@PathVariable String vin) {
        return ResponseEntity.ok(service.unassignOwner(vin));
    }
}
