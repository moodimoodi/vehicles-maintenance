package com.autotech.maintenance.command.domain.handler;

import com.autotech.maintenance.command.domain.aggregate.MaintenanceAppointmentAgrregate;
import com.autotech.maintenance.command.domain.command.*;
import com.autotech.maintenance.command.domain.repository.MaintenanceAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceCommandHandler {

    private final MaintenanceAppointmentRepository repository;

    public MaintenanceAppointmentAgrregate handle(ScheduleMaintenanceCommand cmd) {
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
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

    public MaintenanceAppointmentAgrregate handle(RescheduleMaintenanceCommand cmd) {
        MaintenanceAppointmentAgrregate agg = repository.load(cmd.appointmentId());
        agg.reschedule(cmd.newScheduledAt());
        repository.save(agg);
        return agg;
    }

    public MaintenanceAppointmentAgrregate handle(ChangeStatusCommand cmd) {
        MaintenanceAppointmentAgrregate agg = repository.load(cmd.appointmentId());
        agg.changeStatus(cmd.newStatus());
        repository.save(agg);
        return agg;
    }

    public MaintenanceAppointmentAgrregate handle(CancelMaintenanceCommand cmd) {
        MaintenanceAppointmentAgrregate agg = repository.load(cmd.appointmentId());
        agg.cancel(cmd.reason());
        repository.save(agg);
        return agg;
    }

    public MaintenanceAppointmentAgrregate handle(CompleteMaintenanceCommand cmd) {
        MaintenanceAppointmentAgrregate agg = repository.load(cmd.appointmentId());
        agg.complete(cmd.completedAt() != null ? cmd.completedAt() : LocalDateTime.now());
        repository.save(agg);
        return agg;
    }
}
