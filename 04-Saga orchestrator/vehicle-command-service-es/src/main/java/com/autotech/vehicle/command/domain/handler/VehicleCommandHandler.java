package com.autotech.vehicle.command.domain.handler;

import com.autotech.vehicle.command.domain.aggregate.VehicleAggregate;
import com.autotech.vehicle.command.domain.command.*;
import com.autotech.vehicle.command.domain.repository.VehicleAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleCommandHandler {

        private final VehicleAggregateRepository repository;

        public VehicleAggregate handle(RegisterVehicleCommand cmd) {
        VehicleAggregate agg = VehicleAggregate.registerNew(cmd.vin(),
                cmd.brand(),
                cmd.model(),
                cmd.year(),
                cmd.mileage(),
                cmd.status(),
                cmd.ownerId());
        repository.save(agg);
        return agg;
    }

        public VehicleAggregate handle(ChangeVehicleStatusCommand cmd) {
        VehicleAggregate agg = repository.load(cmd.vin());
        agg.changeStatus(cmd.newStatus());
        repository.save(agg);
        return agg;
    }

        public VehicleAggregate handle(UpdateVehicleDetailsCommand cmd) {
        VehicleAggregate agg = repository.load(cmd.vin());
        agg.updateDetails(cmd.brand(), cmd.model(), cmd.year(), cmd.mileage());
        repository.save(agg);
        return agg;
    }

        public VehicleAggregate handle(AssignOwnerCommand cmd) {
        VehicleAggregate agg = repository.load(cmd.vin());
        agg.assignOwner(cmd.ownerId());
        repository.save(agg);
        return agg;
    }

        public VehicleAggregate handle(UnassignOwnerCommand cmd) {
        VehicleAggregate agg = repository.load(cmd.vin());
        agg.unassignOwner();
        repository.save(agg);
        return agg;
    }
}
