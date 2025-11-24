package com.autotech.maintenance.query.service;

import com.autotech.maintenance.query.dto.MaintenanceViewResponse;
import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import com.autotech.maintenance.query.mapper.MaintenanceViewMapper;
import com.autotech.maintenance.query.repository.MaintenanceAppointmentViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test du service query :
 * - mapping entity → DTO
 * - interactions avec le repository
 * - gestion des cas "not found"
 */
class MaintenanceQueryServiceImplTest {

    private MaintenanceAppointmentViewRepository repository;
    private MaintenanceViewMapper mapper;
    private MaintenanceQueryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(MaintenanceAppointmentViewRepository.class);
        mapper = mock(MaintenanceViewMapper.class);
        service = new MaintenanceQueryServiceImpl(repository, mapper);
    }

    @Test
    void getById_shouldReturnDto() {
        MaintenanceAppointmentViewEntity entity = new MaintenanceAppointmentViewEntity();
        entity.setAppointmentId("APP-123");
        entity.setVehicleVin("VIN-111");
        entity.setCustomerId("CUST-1");
        entity.setWorkshopId("WORK-9");
        entity.setStatus("SCHEDULED");
        entity.setScheduledAt(LocalDateTime.now());

        MaintenanceViewResponse dto = new MaintenanceViewResponse(
                "APP-123", "VIN-111", "CUST-1", "WORK-9",
                entity.getScheduledAt(), "SCHEDULED", null, null
        );

        when(repository.findById("APP-123")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        MaintenanceViewResponse result = service.getById("APP-123");

        assertEquals("APP-123", result.getAppointmentId());
        assertEquals("VIN-111", result.getVehicleVin());
        verify(repository).findById("APP-123");
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(repository.findById("APP-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById("APP-999"));
    }

    @Test
    void getByVehicle_shouldReturnList() {
        MaintenanceAppointmentViewEntity entity = new MaintenanceAppointmentViewEntity();
        entity.setAppointmentId("APP-123");

        MaintenanceViewResponse dto = new MaintenanceViewResponse(
                "APP-123", "VIN-111", "CUST-1", "WORK-9",
                LocalDateTime.now(), "SCHEDULED", null, null
        );

        when(repository.findByCustomerId("CUST-1")).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        var list = service.getByCustomer("CUST-1");

        assertEquals(1, list.size());
        assertEquals("APP-123", list.get(0).getAppointmentId());
    }
}
