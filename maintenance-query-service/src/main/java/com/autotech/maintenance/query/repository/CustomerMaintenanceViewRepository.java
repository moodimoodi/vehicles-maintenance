package com.autotech.maintenance.query.repository;

import com.autotech.maintenance.query.entity.CustomerMaintenanceViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerMaintenanceViewRepository extends JpaRepository<CustomerMaintenanceViewEntity, String> {

    List<CustomerMaintenanceViewEntity> findByCustomerIdOrderByDateDesc(String customerId);
}
