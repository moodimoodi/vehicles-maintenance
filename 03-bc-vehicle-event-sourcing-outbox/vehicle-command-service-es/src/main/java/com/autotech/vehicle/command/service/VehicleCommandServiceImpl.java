package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.domain.aggregate.Vehicle;
import com.autotech.vehicle.command.domain.repository.VehicleAggregateRepository;
import com.autotech.vehicle.command.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleAggregateRepository repository;

    @Override
    public VehicleResponse register(VehicleRegisterRequest request) {
        Vehicle vehicle = Vehicle.registerNew(
                request.getVin(),
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getMileage(),
                request.getStatus(),
                request.getOwnerId()
        );
        repository.save(vehicle);
        return toResponse(vehicle);
    }

    @Override
    public VehicleResponse updateDetails(String vin, VehicleUpdateDetailsRequest request) {
        Vehicle vehicle = repository.load(vin);
        vehicle.updateDetails(
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getMileage()
        );
        repository.save(vehicle);
        return toResponse(vehicle);
    }

    @Override
    public VehicleResponse changeStatus(String vin, VehicleChangeStatusRequest request) {
        Vehicle vehicle = repository.load(vin);
        vehicle.changeStatus(request.getNewStatus());
        repository.save(vehicle);
        return toResponse(vehicle);
    }

    @Override
    public VehicleResponse assignOwner(String vin, VehicleAssignOwnerRequest request) {
        Vehicle vehicle = repository.load(vin);
        vehicle.assignOwner(request.getOwnerId());
        repository.save(vehicle);
        return toResponse(vehicle);
    }

    @Override
    public VehicleResponse unassignOwner(String vin) {
        Vehicle vehicle = repository.load(vin);
        vehicle.unassignOwner();
        repository.save(vehicle);
        return toResponse(vehicle);
    }

    private VehicleResponse toResponse(Vehicle agg) {
        VehicleResponse res = new VehicleResponse();
        res.setVin(agg.getVin());
        res.setBrand(agg.getBrand());
        res.setModel(agg.getModel());
        res.setYear(agg.getYear());
        res.setMileage(agg.getMileage());
        res.setStatus(agg.getStatus());
        res.setOwnerId(agg.getOwnerId());
        res.setVersion(agg.getVersion());
        return res;
    }
}
