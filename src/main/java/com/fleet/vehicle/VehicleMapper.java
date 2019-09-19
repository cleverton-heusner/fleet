package com.fleet.vehicle;

import com.fleet.vehicle_type.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public VehicleDTO toDTO(final VehicleDomain vehicleDomain) {
        VehicleDomain.VehicleId vehicleId = vehicleDomain.getId();

        return VehicleDTO.builder()
                .chassisNumber(vehicleId.getChassisNumber())
                .chassisSeries(vehicleId.getChassisSeries())
                .color(vehicleDomain.getColor())
                .passengersNumber(vehicleDomain.getPassengersNumber())
                .type(vehicleDomain.getType().getName())
                .build();
    }

    public VehicleDomain toDomain(final VehicleDTO vehicleDTO) {
        VehicleDomain.VehicleId vehicleId = VehicleDomain.VehicleId.builder()
                .chassisNumber(vehicleDTO.getChassisNumber())
                .chassisSeries(vehicleDTO.getChassisSeries())
                .build();

        return VehicleDomain.builder()
                .id(vehicleId)
                .color(vehicleDTO.getColor())
                .type(VehicleType.fromName(vehicleDTO.getType()).get())
                .build();
    }
}
