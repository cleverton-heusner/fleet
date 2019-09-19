package com.fleet.vehicle;

import com.fleet.exception.ConflictException;
import com.fleet.exception.NotFoundException;
import com.fleet.vehicle.VehicleDomain;
import com.fleet.vehicle.VehicleDTO;
import com.fleet.vehicle.VehicleIdDecodification;
import com.fleet.vehicle.VehicleMapper;
import com.fleet.vehicle.VehicleRepository;
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
        VehicleDomain vehicleDomainToInsert = vehicleMapper.toDomain(vehicleDTO);

        if (vehicleRepository.existsById(vehicleDomainToInsert.getId())) {
            throw new ConflictException(vehicleValidationMessagesHandler.getChassisInUse());
        }

        VehicleDomain insertedVehicleDomain = vehicleRepository.save(vehicleDomainToInsert);
        return vehicleMapper.toDTO(insertedVehicleDomain);
    }

    public VehicleDTO findById(final String id) throws NotFoundException {
        VehicleDomain.VehicleId vehicleId = vehicleIdDecodification.decode(id);
        Optional<VehicleDomain> vehicle = vehicleRepository.findById(vehicleId);
        if (!vehicle.isPresent()) {
            throw new NotFoundException(vehicleValidationMessagesHandler.getNotFound());
        }

        return vehicleMapper.toDTO(vehicle.get());
    }

    public Page<VehicleDTO> list(final Pageable pagination) {
        Page<VehicleDomain> vehiclesPage = vehicleRepository.findAll(pagination);
        return vehiclesPage.map(vehicle -> vehicleMapper.toDTO(vehicle));
    }

    public VehicleDTO changeColor(final String id, final String newColor) throws NotFoundException {
        VehicleDomain.VehicleId vehicleId = vehicleIdDecodification.decode(id);
        Optional<VehicleDomain> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (!optionalVehicle.isPresent()) {
            throw new NotFoundException(vehicleValidationMessagesHandler.getNotFound());
        }

        VehicleDomain vehicleDomain = optionalVehicle.get();
        vehicleDomain.setColor(newColor);
        VehicleDomain updatedVehicleDomain = vehicleRepository.save(vehicleDomain);
        return vehicleMapper.toDTO(updatedVehicleDomain);
    }

    public void deleteById(final String id) throws NotFoundException {
        VehicleDomain.VehicleId vehicleId = vehicleIdDecodification.decode(id);
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new NotFoundException(vehicleValidationMessagesHandler.getNotFound());
        }

        vehicleRepository.deleteById(vehicleId);
    }
}