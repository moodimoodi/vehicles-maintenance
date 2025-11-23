package com.autotech.maintenance.command.exception;

import com.autotech.maintenance.command.constants.MaintenanceConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaintenanceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(MaintenanceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "code", MaintenanceConstants.ERROR_APPOINTMENT_NOT_FOUND,
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(MaintenanceBusinessException.class)
    public ResponseEntity<?> handleBusiness(MaintenanceBusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "code", ex.getCode(),
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "code", "INTERNAL_ERROR",
                        "message", ex.getMessage()
                ));
    }
}
