package com.autotech.maintenance.command.controller;

import com.autotech.maintenance.command.dto.MaintenanceAppointmentRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceAppointmentResponseDto;
import com.autotech.maintenance.command.dto.MaintenanceStatusUpdateRequestDto;
import com.autotech.maintenance.command.service.MaintenanceCommandService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api/maintenance/commands")
public class MaintenanceCommandController {

    private final MaintenanceCommandService service;

    public MaintenanceCommandController(MaintenanceCommandService service) {
        this.service = service;
    }

    @PostMapping("/schedule")
    public ResponseEntity<MaintenanceAppointmentResponseDto> schedule(
            @RequestBody MaintenanceAppointmentRequestDto request) {

        MaintenanceAppointmentResponseDto response = service.scheduleAppointment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(
            @RequestBody MaintenanceStatusUpdateRequestDto request) {
        service.updateStatus(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete/{appointmentId}")
    public ResponseEntity<Void> complete(
            @PathVariable String appointmentId,
            @RequestParam int finalMileage) {
        service.completeAppointment(appointmentId, finalMileage);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workshop/{workshopId}/planning")
    public ResponseEntity<MaintenanceAppointmentResponseDto> findByWorkshopIdAndDate(
            @PathVariable String workshopId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        service.findByWorkshopIdAndDate(workshopId, date);
        return ResponseEntity.ok().build();
    }
}
