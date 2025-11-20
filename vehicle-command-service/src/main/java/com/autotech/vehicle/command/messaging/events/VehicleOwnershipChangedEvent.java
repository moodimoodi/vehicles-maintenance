package com.autotech.vehicle.command.messaging.events;

import lombok.Data;

@Data
public class VehicleOwnershipChangedEvent {

    private String vin;
    private String oldCustomerId;
    private String newCustomerId;
}
