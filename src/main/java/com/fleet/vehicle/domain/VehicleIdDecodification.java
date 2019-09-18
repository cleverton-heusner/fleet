package com.fleet.vehicle.domain;

import com.fleet.vehicle.validation.VehicleIdValidator;
import org.springframework.stereotype.Component;

@Component
public class VehicleIdDecodification {

    public Vehicle.VehicleId decode(final String codedId) {
        String[] chassisInfo = codedId.split(VehicleIdValidator.VEHICLE_ID_SPLITTER);
        String chassisSeries = chassisInfo[0];
        String chassisNumber = chassisInfo[1];

        return Vehicle.VehicleId.builder()
                .chassisSeries(chassisSeries)
                .chassisNumber(Integer.parseInt(chassisNumber))
                .build();
    }
}
