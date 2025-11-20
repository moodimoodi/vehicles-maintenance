package com.autotech.vehicle.query.repository;

import com.autotech.vehicle.query.entity.CustomerGarageViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerGarageViewRepository extends JpaRepository<CustomerGarageViewEntity, String> {

    List<CustomerGarageViewEntity> findByCustomerId(String customerId);
}
