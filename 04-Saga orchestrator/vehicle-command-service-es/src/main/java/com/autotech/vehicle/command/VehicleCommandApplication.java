package com.autotech.vehicle.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehicleCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleCommandApplication.class, args);
    }
}
