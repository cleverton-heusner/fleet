package com.fleet.vehicle_type;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum VehicleType {

    BUS("Ônibus", (byte) 42),
    CAR("Carro", (byte) 4),
    TRUCK("Caminhão", (byte) 1);

    private String name;
    private byte passengersNumber;

    VehicleType(final String name, final byte passengersNumber) {
        this.name = name;
        this.passengersNumber = passengersNumber;
    }

    public static Optional<VehicleType> fromName(final String name) {
        return Arrays.stream(VehicleType.values())
                .filter(vehicleType -> vehicleType.name.equals(name))
                .findFirst();
    }
}