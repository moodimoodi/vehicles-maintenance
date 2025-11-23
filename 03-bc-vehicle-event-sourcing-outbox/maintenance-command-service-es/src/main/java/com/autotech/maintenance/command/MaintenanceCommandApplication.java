package com.autotech.maintenance.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MaintenanceCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaintenanceCommandApplication.class, args);
    }
}
