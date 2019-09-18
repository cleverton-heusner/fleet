package com.fleet.vehicle.domain;

import com.fleet.vehicle_type.domain.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public VehicleDTO toDTO(final Vehicle vehicle) {
        Vehicle.VehicleId vehicleId = vehicle.getId();

        return VehicleDTO.builder()
                .chassisNumber(vehicleId.getChassisNumber())
                .chassisSeries(vehicleId.getChassisSeries())
                .color(vehicle.getColor())
                .passengersNumber(vehicle.getPassengersNumber())
                .type(vehicle.getType().getName())
                .build();
    }

    public Vehicle toDomain(final VehicleDTO vehicleDTO) {
        Vehicle.VehicleId vehicleId = Vehicle.VehicleId.builder()
                .chassisNumber(vehicleDTO.getChassisNumber())
                .chassisSeries(vehicleDTO.getChassisSeries())
                .build();

        return Vehicle.builder()
                .id(vehicleId)
                .color(vehicleDTO.getColor())
                .type(VehicleType.fromName(vehicleDTO.getType()).get())
                .build();
    }
}
