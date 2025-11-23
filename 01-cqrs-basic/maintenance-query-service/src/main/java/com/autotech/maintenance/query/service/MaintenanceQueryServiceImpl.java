package com.autotech.maintenance.query.service;

import com.autotech.maintenance.query.dto.CustomerMaintenanceViewResponseDto;
import com.autotech.maintenance.query.dto.WorkshopDailyPlanningViewResponseDto;
import com.autotech.maintenance.query.mapper.MaintenanceViewMapper;
import com.autotech.maintenance.query.repository.CustomerMaintenanceViewRepository;
import com.autotech.maintenance.query.repository.WorkshopDailyPlanningViewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceQueryServiceImpl implements MaintenanceQueryService {

    private final CustomerMaintenanceViewRepository customerRepo;
    private final WorkshopDailyPlanningViewRepository workshopRepo;
    private final MaintenanceViewMapper mapper;

    public MaintenanceQueryServiceImpl(CustomerMaintenanceViewRepository customerRepo,
                                       WorkshopDailyPlanningViewRepository workshopRepo,
                                       MaintenanceViewMapper mapper) {
        this.customerRepo = customerRepo;
        this.workshopRepo = workshopRepo;
        this.mapper = mapper;
    }

    @Override
    public List<CustomerMaintenanceViewResponseDto> getCustomerAppointments(String customerId) {
        return customerRepo.findByCustomerIdOrderByDateDesc(customerId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<WorkshopDailyPlanningViewResponseDto> getWorkshopPlanning(String workshopId, LocalDate date) {
        return workshopRepo.findByWorkshopIdAndDateOrderBySlotAsc(workshopId, date)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
