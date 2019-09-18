package com.fleet.vehicle.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VehicleIdValidator implements ConstraintValidator<VehicleId, String> {

    public static final String VEHICLE_ID_SPLITTER = "_";
    private final String CHASSIS_SERIES = ".+";
    private final String CHASSIS_NUMBER = "\\d+";
    private final String VEHICLE_ID_PATTERN = CHASSIS_SERIES + VEHICLE_ID_SPLITTER + CHASSIS_NUMBER;

    @Override
    public boolean isValid(String vehicleId, ConstraintValidatorContext cxt) {
        return !StringUtils.isBlank(vehicleId) && vehicleId.matches(VEHICLE_ID_PATTERN);
    }
}
