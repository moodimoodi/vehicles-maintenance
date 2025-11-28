package com.autotech.maintenance.saga.controller;

import com.autotech.maintenance.saga.controller.dto.StartMaintenanceBookingRequestDto;
import com.autotech.maintenance.saga.controller.dto.StartMaintenanceBookingResponseDto;
import com.autotech.maintenance.saga.service.MaintenanceBookingSagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sagas/maintenance-booking")
@RequiredArgsConstructor
public class SagaController {

    private final MaintenanceBookingSagaService sagaService;

    /**
     * Initier le Saga
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<StartMaintenanceBookingResponseDto> startSaga(
            @RequestBody StartMaintenanceBookingRequestDto request) {
        return ResponseEntity.ok(sagaService.startSaga(request));
    }
}
