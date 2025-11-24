package com.autotech.vehicle.query.service;

import com.autotech.vehicle.query.dto.VehicleViewResponseDto;
import com.autotech.vehicle.query.mapper.VehicleViewMapper;
import com.autotech.vehicle.query.repository.VehicleViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleViewRepository repository;
    private final VehicleViewMapper mapper;

    @Override
    public VehicleViewResponseDto getByVin(String vin) {
        return repository.findById(vin)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vin));
    }

    @Override
    public List<VehicleViewResponseDto> getByOwner(String ownerId) {
        return repository.findByOwnerId(ownerId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<VehicleViewResponseDto> getByStatus(String status) {
        return repository.findByStatus(status)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
