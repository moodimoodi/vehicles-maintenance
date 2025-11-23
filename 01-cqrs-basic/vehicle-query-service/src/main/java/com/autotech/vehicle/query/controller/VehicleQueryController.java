package com.autotech.vehicle.query.controller;

import com.autotech.vehicle.query.dto.CustomerVehicleResponseDto;
import com.autotech.vehicle.query.dto.VehicleOverviewResponseDto;
import com.autotech.vehicle.query.service.VehicleQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles/queries")
public class VehicleQueryController {

    private final VehicleQueryService service;

    public VehicleQueryController(VehicleQueryService service) {
        this.service = service;
    }

    @GetMapping("/{vin}")
    public ResponseEntity<VehicleOverviewResponseDto> getVehicle(@PathVariable String vin) {
        return ResponseEntity.ok(service.getVehicleByVin(vin));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerVehicleResponseDto>> getCustomerVehicles(@PathVariable String customerId) {
        return ResponseEntity.ok(service.getCustomerVehicles(customerId));
    }
}
