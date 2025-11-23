package com.autotech.vehicle.query.repository;

import com.autotech.vehicle.query.entity.VehicleViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleViewRepository extends JpaRepository<VehicleViewEntity, String> {

    List<VehicleViewEntity> findByOwnerId(String ownerId);

    List<VehicleViewEntity> findByStatus(String status);
}
