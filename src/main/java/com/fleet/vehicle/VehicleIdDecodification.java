package com.fleet.vehicle;

import com.fleet.vehicle.validation.VehicleIdValidator;
import org.springframework.stereotype.Component;

@Component
public class VehicleIdDecodification {

    public VehicleDomain.VehicleId decode(final String codedId) {
        String[] chassisInfo = codedId.split(VehicleIdValidator.VEHICLE_ID_SPLITTER);
        String chassisSeries = chassisInfo[0];
        String chassisNumber = chassisInfo[1];

        return VehicleDomain.VehicleId.builder()
                .chassisSeries(chassisSeries)
                .chassisNumber(Integer.parseInt(chassisNumber))
                .build();
    }
}
