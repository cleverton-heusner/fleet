package com.fleet.vehicle_type.domain;


import com.fleet.vehicle_type.domain.VehicleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class VehicleTypeConverter implements AttributeConverter<VehicleType, String> {

    @Override
    public String convertToDatabaseColumn(final VehicleType vehicleType) {
        return String.valueOf(vehicleType.getName());
    }

    @Override
    public VehicleType convertToEntityAttribute(final String name) {
        return VehicleType.fromName(name).get();
    }
}