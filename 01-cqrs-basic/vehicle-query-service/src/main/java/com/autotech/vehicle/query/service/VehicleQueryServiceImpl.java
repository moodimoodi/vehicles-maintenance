package com.autotech.vehicle.query.service;

import com.autotech.vehicle.query.dto.CustomerVehicleResponseDto;
import com.autotech.vehicle.query.dto.VehicleOverviewResponseDto;
import com.autotech.vehicle.query.mapper.VehicleViewMapper;
import com.autotech.vehicle.query.repository.CustomerGarageViewRepository;
import com.autotech.vehicle.query.repository.VehicleOverviewViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleOverviewViewRepository overviewRepo;
    private final CustomerGarageViewRepository garageRepo;
    private final VehicleViewMapper mapper;

    public VehicleQueryServiceImpl(VehicleOverviewViewRepository overviewRepo,
                                   CustomerGarageViewRepository garageRepo,
                                   VehicleViewMapper mapper) {
        this.overviewRepo = overviewRepo;
        this.garageRepo = garageRepo;
        this.mapper = mapper;
    }

    @Override
    public VehicleOverviewResponseDto getVehicleByVin(String vin) {
        return overviewRepo.findById(vin)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for VIN " + vin));
    }

    @Override
    public List<CustomerVehicleResponseDto> getCustomerVehicles(String customerId) {
        return garageRepo.findByCustomerId(customerId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
