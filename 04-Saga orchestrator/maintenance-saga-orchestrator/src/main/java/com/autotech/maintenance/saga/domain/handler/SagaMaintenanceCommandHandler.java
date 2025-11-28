package com.autotech.maintenance.saga.domain.handler;

import com.autotech.maintenance.saga.domain.aggregate.MaintenanceBookingSagaAggregate;
import com.autotech.maintenance.saga.domain.command.CancelMaintenanceBookingCommand;
import com.autotech.maintenance.saga.domain.command.MaintenanceScheduleResultCommand;
import com.autotech.maintenance.saga.domain.command.StartMaintenanceBookingCommand;
import com.autotech.maintenance.saga.domain.command.VehicleReserveResultCommand;
import com.autotech.maintenance.saga.domain.saga.repository.MaintenanceBookingSagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SagaMaintenanceCommandHandler {

    private final MaintenanceBookingSagaRepository repository;

    /**
     * Handle the Start of the booking
     * @param cmd
     * @return
     */
    public MaintenanceBookingSagaAggregate handle(StartMaintenanceBookingCommand cmd) {
        MaintenanceBookingSagaAggregate agg = MaintenanceBookingSagaAggregate.start(
                cmd.sagaId(),
                cmd.appointmentId(),
                cmd.vehicleVin(),
                cmd.customerId(),
                cmd.workshopId(),
                cmd.scheduledAt(),
                cmd.maintenanceType()
        );
        repository.save(agg);
        return agg;
    }

    /**
     * Handle the vehicle reservation and manage the response (Success, or unsuccess)
     * @param command
     * @return
     */
    public MaintenanceBookingSagaAggregate handle(VehicleReserveResultCommand command) {
        MaintenanceBookingSagaAggregate saga = repository.load(command.sagaId());
        if (command.success()) {
            saga.onVehicleReserved();
        } else {
            saga.onVehicleReservationFailed(command.reason());
        }
        repository.save(saga);
        return saga;
    }

    /**
     * Handle the maintenance scheduling and manage the response (success o unsecces)
     * @param command
     */
    public MaintenanceBookingSagaAggregate handle(MaintenanceScheduleResultCommand command) {
        MaintenanceBookingSagaAggregate saga = repository.load(command.sagaId());
        if (command.success()) {
            saga.onMaintenanceScheduled();
        } else {
            saga.onMaintenanceScheduleFailed(command.reason());
        }
        repository.save(saga);
        return saga;
    }

    public void handle(CancelMaintenanceBookingCommand command) {
        // TODO: add explicit cancel behaviour in aggregate if needed
        repository.load(command.sagaId());
    }
}
