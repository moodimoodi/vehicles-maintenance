package com.autotech.vehicle.command.web;

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
    public ResponseEntity<VehicleResponse> register(@RequestBody VehicleRegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/{vin}/details")
    public ResponseEntity<VehicleResponse> updateDetails(
            @PathVariable String vin,
            @RequestBody VehicleUpdateDetailsRequest request) {
        return ResponseEntity.ok(service.updateDetails(vin, request));
    }

    @PostMapping("/{vin}/status")
    public ResponseEntity<VehicleResponse> changeStatus(
            @PathVariable String vin,
            @RequestBody VehicleChangeStatusRequest request) {
        return ResponseEntity.ok(service.changeStatus(vin, request));
    }

    @PostMapping("/{vin}/owner")
    public ResponseEntity<VehicleResponse> assignOwner(
            @PathVariable String vin,
            @RequestBody VehicleAssignOwnerRequest request) {
        return ResponseEntity.ok(service.assignOwner(vin, request));
    }

    @PostMapping("/{vin}/owner/unassign")
    public ResponseEntity<VehicleResponse> unassignOwner(@PathVariable String vin) {
        return ResponseEntity.ok(service.unassignOwner(vin));
    }
}
