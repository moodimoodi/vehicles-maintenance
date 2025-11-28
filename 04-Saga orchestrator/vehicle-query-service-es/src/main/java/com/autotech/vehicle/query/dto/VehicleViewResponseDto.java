package com.autotech.vehicle.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleViewResponseDto {

    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
    private String status;
    private String ownerId;
    private LocalDateTime lastUpdatedAt;
}
