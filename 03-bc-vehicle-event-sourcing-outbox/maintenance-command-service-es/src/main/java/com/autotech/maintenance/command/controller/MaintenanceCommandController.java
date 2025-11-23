package com.autotech.maintenance.command.controller;

import com.autotech.maintenance.command.dto.*;
import com.autotech.maintenance.command.service.MaintenanceCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance/commands")
@RequiredArgsConstructor
public class MaintenanceCommandController {

    private final MaintenanceCommandService service;

    @PostMapping
    public ResponseEntity<MaintenanceResponseDto> schedule(@RequestBody MaintenanceScheduleRequestDto request) {
        return ResponseEntity.ok(service.schedule(request));
    }

    @PostMapping("/{appointmentId}/reschedule")
    public ResponseEntity<MaintenanceResponseDto> reschedule(
            @PathVariable String appointmentId,
            @RequestBody MaintenanceRescheduleRequestDto request) {
        return ResponseEntity.ok(service.reschedule(appointmentId, request));
    }

    @PostMapping("/{appointmentId}/status")
    public ResponseEntity<MaintenanceResponseDto> changeStatus(
            @PathVariable String appointmentId,
            @RequestBody MaintenanceChangeStatusRequestDto request) {
        return ResponseEntity.ok(service.changeStatus(appointmentId, request));
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<MaintenanceResponseDto> cancel(
            @PathVariable String appointmentId,
            @RequestParam(name = "reason", required = false, defaultValue = "N/A") String reason) {
        return ResponseEntity.ok(service.cancel(appointmentId, reason));
    }

    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<MaintenanceResponseDto> complete(@PathVariable String appointmentId) {
        return ResponseEntity.ok(service.complete(appointmentId));
    }
}
