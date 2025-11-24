package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.domain.aggregate.VehicleAggregate;
import com.autotech.vehicle.command.domain.command.*;
import com.autotech.vehicle.command.domain.handler.VehicleCommandHandler;
import com.autotech.vehicle.command.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleCommandHandler handler;

    @Override
    public VehicleResponseDto register(VehicleRegisterRequestDto request) {
        RegisterVehicleCommand cmd = new RegisterVehicleCommand(
                request.getVin(),
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getMileage(),
                request.getStatus(),
                request.getOwnerId()
        );

        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public VehicleResponseDto updateDetails(String vin, VehicleUpdateDetailsRequestDto request) {
        UpdateVehicleDetailsCommand cmd = new UpdateVehicleDetailsCommand(
                vin,
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getMileage()
        );
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public VehicleResponseDto changeStatus(String vin, VehicleChangeStatusRequestDto request) {
        ChangeVehicleStatusCommand cmd = new ChangeVehicleStatusCommand(
                vin,
                request.getNewStatus()
                );
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public VehicleResponseDto assignOwner(String vin, VehicleAssignOwnerRequestDto request) {
        AssignOwnerCommand cmd = new AssignOwnerCommand(
                vin,
                request.getOwnerId()
        );
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public VehicleResponseDto unassignOwner(String vin) {
        UnassignOwnerCommand cmd = new UnassignOwnerCommand(vin);
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    private VehicleResponseDto toResponse(VehicleAggregate agg) {
        VehicleResponseDto res = new VehicleResponseDto();
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
