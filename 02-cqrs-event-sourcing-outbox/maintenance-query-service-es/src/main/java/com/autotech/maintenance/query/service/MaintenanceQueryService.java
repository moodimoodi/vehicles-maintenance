package com.autotech.maintenance.query.service;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;

import java.util.List;

public interface MaintenanceQueryService {

    MaintenanceViewResponse getById(String appointmentId);

    List<MaintenanceViewResponse> getByCustomer(String customerId);

    List<MaintenanceViewResponse> getByWorkshop(String workshopId);
}
