package com.autotech.maintenance.query.controller;

import com.autotech.maintenance.query.dto.CustomerMaintenanceViewResponseDto;
import com.autotech.maintenance.query.dto.WorkshopDailyPlanningViewResponseDto;
import com.autotech.maintenance.query.service.MaintenanceQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance/queries")
public class MaintenanceQueryController {

    private final MaintenanceQueryService service;

    public MaintenanceQueryController(MaintenanceQueryService service) {
        this.service = service;
    }

    @GetMapping("/customer/{customerId}/appointments")
    public ResponseEntity<List<CustomerMaintenanceViewResponseDto>> getCustomerAppointments(
            @PathVariable String customerId) {
        return ResponseEntity.ok(service.getCustomerAppointments(customerId));
    }

    @GetMapping("/workshop/{workshopId}/planning")
    public ResponseEntity<List<WorkshopDailyPlanningViewResponseDto>> getWorkshopPlanning(
            @PathVariable String workshopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getWorkshopPlanning(workshopId, date));
    }
}
