package com.autotech.vehicle.query.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleOverviewResponseDto {

    private String vin;
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private String color;
    private Integer currentMileage;
    private String customerId;
    private LocalDate registrationDate;
}
