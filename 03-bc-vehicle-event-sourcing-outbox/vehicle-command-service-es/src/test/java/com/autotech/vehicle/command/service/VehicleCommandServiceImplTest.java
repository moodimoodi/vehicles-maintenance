package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.domain.aggregate.VehicleAggregate;
import com.autotech.vehicle.command.domain.command.*;
import com.autotech.vehicle.command.domain.handler.VehicleCommandHandler;
import com.autotech.vehicle.command.dto.*;
import com.autotech.vehicle.command.service.VehicleCommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Tests de la couche Application : on vérifie que
 *  - le service construit le bon Command
 *  - le service délègue bien au VehicleCommandHandler
 *  - la réponse renvoyée au contrôleur correspond à l’aggregate renvoyé par le handler
 */
class VehicleCommandServiceImplTest {

    private VehicleCommandHandler handler;
    private VehicleCommandServiceImpl service;

    @BeforeEach
    void setUp() {
        handler = mock(VehicleCommandHandler.class);
        service = new VehicleCommandServiceImpl(handler);
    }

    // -------------------------------------------------------------------------
    // register()
    // -------------------------------------------------------------------------

    @Test
    void register_shouldBuildRegisterVehicleCommand_andReturnMappedResponse() {
        // Given : un DTO d’entrée
        VehicleRegisterRequestDto request = new VehicleRegisterRequestDto();
        request.setVin("VIN-123");
        request.setBrand("Toyota");
        request.setModel("Corolla");
        request.setYear(2020);
        request.setMileage(30000);
        request.setStatus("ACTIVE");
        request.setOwnerId("CUST-1");

        // Et un aggregate renvoyé par le handler
        VehicleAggregate agg = mock(VehicleAggregate.class);
        when(agg.getVin()).thenReturn("VIN-123");
        when(agg.getBrand()).thenReturn("Toyota");
        when(agg.getModel()).thenReturn("Corolla");
        when(agg.getYear()).thenReturn(2020);
        when(agg.getMileage()).thenReturn(30000);
        when(agg.getStatus()).thenReturn("ACTIVE");
        when(agg.getOwnerId()).thenReturn("CUST-1");
        when(agg.getVersion()).thenReturn(1);

        when(handler.handle(any(RegisterVehicleCommand.class))).thenReturn(agg);

        ArgumentCaptor<RegisterVehicleCommand> captor =
                ArgumentCaptor.forClass(RegisterVehicleCommand.class);

        // When
        VehicleResponseDto response = service.register(request);

        // Then : le handler a bien reçu le bon Command
        verify(handler).handle(captor.capture());
        RegisterVehicleCommand sentCmd = captor.getValue();

        assertEquals("VIN-123", sentCmd.vin());
        assertEquals("Toyota", sentCmd.brand());
        assertEquals("Corolla", sentCmd.model());
        assertEquals(2020, sentCmd.year());
        assertEquals(30000, sentCmd.mileage());
        assertEquals("ACTIVE", sentCmd.status());
        assertEquals("CUST-1", sentCmd.ownerId());

        // Et la réponse correspond à l’aggregate renvoyé par le handler
        assertEquals("VIN-123", response.getVin());
        assertEquals("Toyota", response.getBrand());
        assertEquals("Corolla", response.getModel());
        assertEquals(2020, response.getYear());
        assertEquals(30000, response.getMileage());
        assertEquals("ACTIVE", response.getStatus());
        assertEquals("CUST-1", response.getOwnerId());
        assertEquals(1, response.getVersion());
    }

    // -------------------------------------------------------------------------
    // updateDetails()
    // -------------------------------------------------------------------------

    @Test
    void updateDetails_shouldBuildUpdateVehicleDetailsCommand_andReturnMappedResponse() {
        // Given
        VehicleUpdateDetailsRequestDto request = new VehicleUpdateDetailsRequestDto();
        request.setBrand("Toyota");
        request.setModel("Corolla Hybrid");
        request.setYear(2021);
        request.setMileage(35000);

        VehicleAggregate agg = mock(VehicleAggregate.class);
        when(agg.getVin()).thenReturn("VIN-123");
        when(agg.getBrand()).thenReturn("Toyota");
        when(agg.getModel()).thenReturn("Corolla Hybrid");
        when(agg.getYear()).thenReturn(2021);
        when(agg.getMileage()).thenReturn(35000);
        when(agg.getStatus()).thenReturn("ACTIVE");
        when(agg.getOwnerId()).thenReturn("CUST-1");
        when(agg.getVersion()).thenReturn(2);

        when(handler.handle(any(UpdateVehicleDetailsCommand.class))).thenReturn(agg);

        ArgumentCaptor<UpdateVehicleDetailsCommand> captor =
                ArgumentCaptor.forClass(UpdateVehicleDetailsCommand.class);

        // When
        VehicleResponseDto response = service.updateDetails("VIN-123", request);

        // Then
        verify(handler).handle(captor.capture());
        UpdateVehicleDetailsCommand cmd = captor.getValue();

        assertEquals("VIN-123", cmd.vin());
        assertEquals("Toyota", cmd.brand());
        assertEquals("Corolla Hybrid", cmd.model());
        assertEquals(2021, cmd.year());
        assertEquals(35000, cmd.mileage());

        assertEquals("VIN-123", response.getVin());
        assertEquals("Corolla Hybrid", response.getModel());
        assertEquals(2021, response.getYear());
        assertEquals(35000, response.getMileage());
        assertEquals(2, response.getVersion());
    }

    // -------------------------------------------------------------------------
    // changeStatus()
    // -------------------------------------------------------------------------

    @Test
    void changeStatus_shouldBuildChangeVehicleStatusCommand_andReturnMappedResponse() {
        // Given
        VehicleChangeStatusRequestDto request = new VehicleChangeStatusRequestDto();
        request.setNewStatus("INACTIVE");

        VehicleAggregate agg = mock(VehicleAggregate.class);
        when(agg.getVin()).thenReturn("VIN-123");
        when(agg.getStatus()).thenReturn("INACTIVE");
        when(agg.getVersion()).thenReturn(3);

        when(handler.handle(any(ChangeVehicleStatusCommand.class))).thenReturn(agg);

        ArgumentCaptor<ChangeVehicleStatusCommand> captor =
                ArgumentCaptor.forClass(ChangeVehicleStatusCommand.class);

        // When
        VehicleResponseDto response = service.changeStatus("VIN-123", request);

        // Then
        verify(handler).handle(captor.capture());
        ChangeVehicleStatusCommand cmd = captor.getValue();

        assertEquals("VIN-123", cmd.vin());
        assertEquals("INACTIVE", cmd.newStatus());

        assertEquals("VIN-123", response.getVin());
        assertEquals("INACTIVE", response.getStatus());
        assertEquals(3, response.getVersion());
    }

    // -------------------------------------------------------------------------
    // assignOwner()
    // -------------------------------------------------------------------------

    @Test
    void assignOwner_shouldBuildAssignVehicleOwnerCommand_andReturnMappedResponse() {
        // Given
        VehicleAssignOwnerRequestDto request = new VehicleAssignOwnerRequestDto();
        request.setOwnerId("CUST-99");

        VehicleAggregate agg = mock(VehicleAggregate.class);
        when(agg.getVin()).thenReturn("VIN-123");
        when(agg.getOwnerId()).thenReturn("CUST-99");
        when(agg.getVersion()).thenReturn(4);

        when(handler.handle(any(AssignOwnerCommand.class))).thenReturn(agg);

        ArgumentCaptor<AssignOwnerCommand> captor =
                ArgumentCaptor.forClass(AssignOwnerCommand.class);

        // When
        VehicleResponseDto response = service.assignOwner("VIN-123", request);

        // Then
        verify(handler).handle(captor.capture());
        AssignOwnerCommand cmd = captor.getValue();

        assertEquals("VIN-123", cmd.vin());
        assertEquals("CUST-99", cmd.ownerId());

        assertEquals("VIN-123", response.getVin());
        assertEquals("CUST-99", response.getOwnerId());
        assertEquals(4, response.getVersion());
    }

    // -------------------------------------------------------------------------
    // unassignOwner()
    // -------------------------------------------------------------------------

    @Test
    void unassignOwner_shouldBuildUnassignVehicleOwnerCommand_andReturnMappedResponse() {
        // Given
        VehicleAggregate agg = mock(VehicleAggregate.class);
        when(agg.getVin()).thenReturn("VIN-123");
        when(agg.getOwnerId()).thenReturn(null);
        when(agg.getVersion()).thenReturn(5);

        when(handler.handle(any(UnassignOwnerCommand.class))).thenReturn(agg);

        ArgumentCaptor<UnassignOwnerCommand> captor =
                ArgumentCaptor.forClass(UnassignOwnerCommand.class);

        // When
        VehicleResponseDto response = service.unassignOwner("VIN-123");

        // Then
        verify(handler).handle(captor.capture());
        UnassignOwnerCommand cmd = captor.getValue();

        assertEquals("VIN-123", cmd.vin());

        assertEquals("VIN-123", response.getVin());
        assertNull(response.getOwnerId());
        assertEquals(5, response.getVersion());
    }
}
