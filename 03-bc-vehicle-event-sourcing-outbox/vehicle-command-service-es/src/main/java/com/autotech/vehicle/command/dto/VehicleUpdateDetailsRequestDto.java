package com.autotech.vehicle.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class VehicleUpdateDetailsRequestDto {
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
}
