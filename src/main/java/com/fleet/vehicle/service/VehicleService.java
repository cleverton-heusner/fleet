package com.fleet.vehicle.service;

import com.fleet.exception.ConflictException;
import com.fleet.exception.NotFoundException;
import com.fleet.vehicle.domain.Vehicle;
import com.fleet.vehicle.domain.VehicleDTO;
import com.fleet.vehicle.domain.VehicleIdDecodification;
import com.fleet.vehicle.domain.VehicleMapper;
import com.fleet.vehicle.repository.VehicleRepository;
import com.fleet.vehicle.validation.VehicleValidationMessagesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleValidationMessagesHandler vehicleValidationMessagesHandler;

    @Autowired
    private VehicleIdDecodification vehicleIdDecodification;

    public VehicleDTO insert(final VehicleDTO vehicleDTO) throws ConflictException {
        Vehicle vehicleToInsert = vehicleMapper.toDomain(vehicleDTO);

        if (vehicleRepository.existsById(vehicleToInsert.getId())) {
            throw new ConflictException(vehicleValidationMessagesHandler.getChassisInUse());
        }

        Vehicle insertedVehicle = vehicleRepository.save(vehicleToInsert);
        return vehicleMapper.toDTO(insertedVehicle);
    }

    public VehicleDTO findById(final String id) throws NotFoundException {
        Vehicle.VehicleId vehicleId = vehicleIdDecodification.decode(id);
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (!vehicle.isPresent()) {
            throw new NotFoundException(vehicleValidationMessagesHandler.getNotFound());
        }

        return vehicleMapper.toDTO(vehicle.get());
    }

    public Page<VehicleDTO> list(final Pageable pagination) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pagination);
        return vehiclesPage.map(vehicle -> vehicleMapper.toDTO(vehicle));
    }

    public VehicleDTO changeColor(final String id, final String newColor) throws NotFoundException {
        Vehicle.VehicleId vehicleId = vehicleIdDecodification.decode(id);
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (!optionalVehicle.isPresent()) {
            throw new NotFoundException(vehicleValidationMessagesHandler.getNotFound());
        }

        Vehicle vehicle = optionalVehicle.get();
        vehicle.setColor(newColor);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDTO(updatedVehicle);
    }

    public void deleteById(final String id) throws NotFoundException {
        Vehicle.VehicleId vehicleId = vehicleIdDecodification.decode(id);
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new NotFoundException(vehicleValidationMessagesHandler.getNotFound());
        }

        vehicleRepository.deleteById(vehicleId);
    }
}