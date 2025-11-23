package com.autotech.vehicle.command.dto;

import lombok.Data;

@Data
public class VehicleChangeStatusRequest {
    private String newStatus;
}
