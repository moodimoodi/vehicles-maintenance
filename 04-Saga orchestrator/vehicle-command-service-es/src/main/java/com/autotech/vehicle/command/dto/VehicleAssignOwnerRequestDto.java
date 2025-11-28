package com.autotech.vehicle.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleAssignOwnerRequestDto {
    private String ownerId;
}
