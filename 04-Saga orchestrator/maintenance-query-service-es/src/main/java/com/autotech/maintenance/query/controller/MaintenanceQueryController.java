package com.autotech.maintenance.query.controller;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;
import com.autotech.maintenance.query.service.MaintenanceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance/queries")
@RequiredArgsConstructor
public class MaintenanceQueryController {

    private final MaintenanceQueryService service;

    @GetMapping("/{appointmentId}")
    public ResponseEntity<MaintenanceViewResponse> getById(@PathVariable String appointmentId) {
        return ResponseEntity.ok(service.getById(appointmentId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MaintenanceViewResponse>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(service.getByCustomer(customerId));
    }

    @GetMapping("/workshop/{workshopId}")
    public ResponseEntity<List<MaintenanceViewResponse>> getByWorkshop(@PathVariable String workshopId) {
        return ResponseEntity.ok(service.getByWorkshop(workshopId));
    }
}
