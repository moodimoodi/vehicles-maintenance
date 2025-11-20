package com.autotech.vehicle.command.repository;

import com.autotech.vehicle.command.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {
}
