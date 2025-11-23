package com.autotech.maintenance.query.service;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;
import com.autotech.maintenance.query.mapper.MaintenanceViewMapper;
import com.autotech.maintenance.query.repository.MaintenanceAppointmentViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceQueryServiceImpl implements MaintenanceQueryService {

    private final MaintenanceAppointmentViewRepository repository;
    private final MaintenanceViewMapper mapper;

    @Override
    public MaintenanceViewResponse getById(String appointmentId) {
        return repository.findById(appointmentId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance appointment not found: " + appointmentId));
    }

    @Override
    public List<MaintenanceViewResponse> getByCustomer(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<MaintenanceViewResponse> getByWorkshop(String workshopId) {
        return repository.findByWorkshopId(workshopId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
