package com.autotech.vehicle.query.web;

import com.autotech.vehicle.query.dto.VehicleViewResponse;
import com.autotech.vehicle.query.service.VehicleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles/queries")
@RequiredArgsConstructor
public class VehicleQueryController {

    private final VehicleQueryService service;

    @GetMapping("/{vin}")
    public ResponseEntity<VehicleViewResponse> getByVin(@PathVariable String vin) {
        return ResponseEntity.ok(service.getByVin(vin));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleViewResponse>> getByOwner(@PathVariable String ownerId) {
        return ResponseEntity.ok(service.getByOwner(ownerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VehicleViewResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }
}
