package com.fleet;

import com.fleet.exception.ConflictException;
import com.fleet.exception.NotFoundException;
import com.fleet.vehicle.domain.*;
import com.fleet.vehicle.repository.VehicleRepository;
import com.fleet.vehicle.service.VehicleService;
import com.fleet.vehicle.validation.VehicleValidationMessagesHandler;
import com.fleet.vehicle_type.domain.VehicleType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VehicleUnitTest {

    private final VehicleDTO EXPECTED_VEHICLE_DTO = getExpectedVehicleDto();
    private final String VEHICLE_ID = "ABC_123";
    private final String NEW_COLOR = "green";

    private final String MESSAGE_CHASSIS_IN_USE = "Chassis is in use.";
    private final String MESSAGE_VEHICLE_NOT_FOUND = "Not found.";

    public int pageNum = 0;
    public int pageSize = 1;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleValidationMessagesHandler vehicleValidationMessages;

    @Mock
    private VehicleIdDecodification vehicleIdDecodification;

    @Captor
    private ArgumentCaptor<VehicleDTO> vehicleDTOArgumentCaptor;

    @Captor
    private ArgumentCaptor<Vehicle> vehicleArgumentCaptor;

    @Captor
    private ArgumentCaptor<Vehicle.VehicleId> vehicleIdArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> vehicleCodedIdArgumentCaptor;

    @Test
    public void insertVehicleWhenDetaisAreValid() throws ConflictException {
        Vehicle expectedVehicleDomain = getExpectedVehicleDomain().get();

        when(vehicleMapper.toDomain(EXPECTED_VEHICLE_DTO)).thenReturn(expectedVehicleDomain);
        when(vehicleRepository.existsById(expectedVehicleDomain.getId())).thenReturn(false);
        when(vehicleRepository.save(expectedVehicleDomain)).thenReturn(expectedVehicleDomain);
        when(vehicleMapper.toDTO(expectedVehicleDomain)).thenReturn(EXPECTED_VEHICLE_DTO);

        vehicleService.insert(EXPECTED_VEHICLE_DTO);

        verify(vehicleMapper, times(1)).toDomain(vehicleDTOArgumentCaptor.capture());
        assertThat(vehicleDTOArgumentCaptor.getValue(), is(equalTo(EXPECTED_VEHICLE_DTO)));
        verify(vehicleRepository, times(1)).existsById(vehicleIdArgumentCaptor.capture());
        assertThat(vehicleIdArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain.getId())));
        verify(vehicleRepository, times(1)).save(vehicleArgumentCaptor.capture());
        assertThat(vehicleArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain)));
        verify(vehicleMapper, times(1)).toDTO(vehicleArgumentCaptor.capture());
        assertThat(vehicleArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain)));
    }

    private VehicleDTO getExpectedVehicleDto() {
        return VehicleDTO.builder()
                .chassisNumber(123)
                .chassisSeries("ABC")
                .color("white")
                .type("Carro")
                .build();
    }

    private Optional<Vehicle> getExpectedVehicleDomain() {
        Vehicle.VehicleId vehicleId = Vehicle.VehicleId.builder()
                .chassisNumber(123)
                .chassisSeries("ABC")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .color("white")
                .type(VehicleType.fromName("Carro").get())
                .build();
        return Optional.of(vehicle);
    }

    @Test
    public void failToInsertVehicleWhenChassisIsInUse() throws ConflictException {
        Vehicle expectedVehicleDomain = getExpectedVehicleDomain().get();

        when(vehicleMapper.toDomain(EXPECTED_VEHICLE_DTO)).thenReturn(expectedVehicleDomain);
        when(vehicleRepository.existsById(expectedVehicleDomain.getId())).thenReturn(true);
        when(vehicleValidationMessages.getChassisInUse()).thenReturn(MESSAGE_CHASSIS_IN_USE);

        expectException(ConflictException.class, MESSAGE_CHASSIS_IN_USE);
        vehicleService.insert(EXPECTED_VEHICLE_DTO);
    }

    private void expectException(final Class clazz, final String message) {
        expectedException.expect(clazz);
        expectedException.expectMessage(message);
    }

    @Test
    public void findByIdWhenThereIsVehicle() throws NotFoundException {
        Optional<Vehicle> expectedVehicleDomain = getExpectedVehicleDomain();

        when(vehicleIdDecodification.decode(anyString())).thenReturn(expectedVehicleDomain.get().getId());
        when(vehicleRepository.findById(expectedVehicleDomain.get().getId())).thenReturn(expectedVehicleDomain);
        when(vehicleMapper.toDTO(expectedVehicleDomain.get())).thenReturn(EXPECTED_VEHICLE_DTO);

        vehicleService.findById(VEHICLE_ID);

        verify(vehicleIdDecodification, times(1)).decode(vehicleCodedIdArgumentCaptor.capture());
        assertThat(vehicleCodedIdArgumentCaptor.getValue(), is(equalTo(VEHICLE_ID)));
        verify(vehicleRepository, times(1)).findById(vehicleIdArgumentCaptor.capture());
        assertThat(vehicleIdArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain.get().getId())));
        verify(vehicleMapper, times(1)).toDTO(vehicleArgumentCaptor.capture());
        assertThat(vehicleArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain.get())));
    }

    @Test
    public void failToFindByIdWhenThereIsNotVehicle() throws NotFoundException {
        when(vehicleIdDecodification.decode(anyString())).thenReturn(getExpectedVehicleDomain().get().getId());
        when(vehicleRepository.findById(getExpectedVehicleDomain().get().getId())).thenReturn(Optional.empty());
        when(vehicleValidationMessages.getNotFound()).thenReturn(MESSAGE_VEHICLE_NOT_FOUND);

        expectException(NotFoundException.class, MESSAGE_VEHICLE_NOT_FOUND);
        vehicleService.findById(VEHICLE_ID);
    }

    @Test
    public void listWhenThereAreVehicles() {
        Vehicle expectedVehicleDomain = getExpectedVehicleDomain().get();
        Page<Vehicle> page = new PageImpl(Arrays.asList(expectedVehicleDomain));
        Pageable pagination = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "id");

        when(vehicleRepository.findAll(pagination)).thenReturn(page);
        when(vehicleMapper.toDTO(expectedVehicleDomain)).thenReturn(EXPECTED_VEHICLE_DTO);

        vehicleService.list(pagination);

        verify(vehicleRepository, times(1)).findAll(pagination);
        verify(vehicleMapper, times(1)).toDTO(vehicleArgumentCaptor.capture());
        assertThat(vehicleArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain)));
    }

    @Test
    public void listWhenThereAreNotVehicles() {
        PageRequest pagination = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "id");
        when(vehicleRepository.findAll(pagination)).thenReturn(Page.empty());

        vehicleService.list(pagination);

        verify(vehicleRepository, times(1)).findAll(pagination);
        verify(vehicleMapper, never()).toDTO(getExpectedVehicleDomain().get());
    }

    @Test
    public void changeVehicleColorWhenDetailsAreValid() throws NotFoundException {
        Optional<Vehicle> expectedVehicleDomain = getExpectedVehicleDomain();

        when(vehicleIdDecodification.decode(anyString())).thenReturn(expectedVehicleDomain.get().getId());
        when(vehicleRepository.findById(expectedVehicleDomain.get().getId())).thenReturn(expectedVehicleDomain);
        when(vehicleRepository.save(expectedVehicleDomain.get())).thenReturn(expectedVehicleDomain.get());
        when(vehicleMapper.toDTO(expectedVehicleDomain.get())).thenReturn(EXPECTED_VEHICLE_DTO);

        vehicleService.changeColor(VEHICLE_ID, NEW_COLOR);

        verify(vehicleIdDecodification, times(1)).decode(vehicleCodedIdArgumentCaptor.capture());
        assertThat(vehicleCodedIdArgumentCaptor.getValue(), is(equalTo(VEHICLE_ID)));
        verify(vehicleRepository, times(1)).findById(vehicleIdArgumentCaptor.capture());
        assertThat(vehicleIdArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain.get().getId())));
        verify(vehicleRepository, times(1)).save(vehicleArgumentCaptor.capture());
        assertThat(vehicleArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain.get())));
        verify(vehicleMapper, times(1)).toDTO(vehicleArgumentCaptor.capture());
        assertThat(vehicleArgumentCaptor.getValue(), is(equalTo(expectedVehicleDomain.get())));
    }

    @Test
    public void failToChangeVehicleColorWhenVehicleNotFound() throws NotFoundException {
        when(vehicleIdDecodification.decode(anyString())).thenReturn(getExpectedVehicleDomain().get().getId());
        when(vehicleRepository.findById(getExpectedVehicleDomain().get().getId())).thenReturn(Optional.empty());
        when(vehicleValidationMessages.getNotFound()).thenReturn(MESSAGE_VEHICLE_NOT_FOUND);

        expectException(NotFoundException.class, MESSAGE_VEHICLE_NOT_FOUND);
        vehicleService.changeColor(VEHICLE_ID, NEW_COLOR);
    }

    @Test
    public void deleteVehicleByIdWhenIdIsValid() throws NotFoundException {
        Vehicle.VehicleId expectedVehicleId = getExpectedVehicleDomain().get().getId();

        when(vehicleIdDecodification.decode(anyString())).thenReturn(expectedVehicleId);
        when(vehicleRepository.existsById(expectedVehicleId)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(expectedVehicleId);

        vehicleService.deleteById(VEHICLE_ID);

        verify(vehicleIdDecodification, times(1)).decode(vehicleCodedIdArgumentCaptor.capture());
        assertThat(vehicleCodedIdArgumentCaptor.getValue(), is(equalTo(VEHICLE_ID)));
        verify(vehicleRepository, times(1)).existsById(vehicleIdArgumentCaptor.capture());
        assertThat(vehicleIdArgumentCaptor.getValue(), is(equalTo(expectedVehicleId)));
        verify(vehicleRepository, times(1)).deleteById(vehicleIdArgumentCaptor.capture());
        assertThat(vehicleIdArgumentCaptor.getValue(), is(equalTo(expectedVehicleId)));
    }

    @Test
    public void failtToDeleteVehicleByIdWhenVehicleNotFound() throws NotFoundException {
        when(vehicleIdDecodification.decode(anyString())).thenReturn(getExpectedVehicleDomain().get().getId());
        when(vehicleRepository.existsById(getExpectedVehicleDomain().get().getId())).thenReturn(false);
        when(vehicleValidationMessages.getNotFound()).thenReturn(MESSAGE_VEHICLE_NOT_FOUND);

        expectException(NotFoundException.class, MESSAGE_VEHICLE_NOT_FOUND);
        vehicleService.deleteById(VEHICLE_ID);
    }
}