package com.autotech.vehicle.query.service;

import com.autotech.vehicle.query.dto.VehicleViewResponseDto;
import com.autotech.vehicle.query.entity.VehicleViewEntity;
import com.autotech.vehicle.query.mapper.VehicleViewMapper;
import com.autotech.vehicle.query.repository.VehicleViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VehicleQueryServiceImplTest {

    private VehicleViewRepository repository;
    private VehicleViewMapper mapper;
    private VehicleQueryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(VehicleViewRepository.class);
        mapper = mock(VehicleViewMapper.class);
        service = new VehicleQueryServiceImpl(repository, mapper);
    }

    @Test
    void getByVin_shouldReturnDto() {
        VehicleViewEntity entity = new VehicleViewEntity();
        entity.setVin("VIN-123");
        entity.setBrand("Toyota");
        entity.setModel("Corolla");
        entity.setYear(2020);
        entity.setMileage(30000);
        entity.setStatus("ACTIVE");
        entity.setOwnerId("CUST-1");
        entity.setLastUpdatedAt(LocalDateTime.now());

        VehicleViewResponseDto dto = new VehicleViewResponseDto();
        dto.setVin("VIN-123");
        dto.setBrand("Toyota");
        dto.setModel("Corolla");

        when(repository.findById("VIN-123")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        VehicleViewResponseDto result = service.getByVin("VIN-123");

        assertEquals("VIN-123", result.getVin());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Corolla", result.getModel());
    }

    @Test
    void getByOwner_shouldMapAllEntities() {
        VehicleViewEntity entity = new VehicleViewEntity();
        entity.setVin("VIN-123");

        VehicleViewResponseDto dto = new VehicleViewResponseDto();
        dto.setVin("VIN-123");

        when(repository.findByOwnerId("CUST-1")).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        var result = service.getByOwner("CUST-1");

        assertEquals(1, result.size());
        assertEquals("VIN-123", result.get(0).getVin());
    }

    @Test
    void getByStatus_shouldMapAllEntities() {
        VehicleViewEntity entity = new VehicleViewEntity();
        entity.setVin("VIN-123");

        VehicleViewResponseDto dto = new VehicleViewResponseDto();
        dto.setVin("VIN-123");

        when(repository.findByStatus("ACTIVE")).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        var result = service.getByStatus("ACTIVE");

        assertEquals(1, result.size());
        assertEquals("VIN-123", result.get(0).getVin());
    }
}
