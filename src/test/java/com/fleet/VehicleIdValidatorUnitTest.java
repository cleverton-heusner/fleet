package com.fleet;

import com.fleet.vehicle.validation.VehicleIdValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VehicleIdValidatorUnitTest {

    private final String VEHICLE_ID_SPLITTER = "_";

    private VehicleIdValidator vehicleIdValidator = new VehicleIdValidator();
    private String chassisSeries;
    private String chassisNumber;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void decodeVehicleIdWhenThisIsValid() {
        chassisSeries = "ABC";
        chassisNumber = "123";

        boolean isValid = vehicleIdValidator.isValid(getVehicleCodedId(), null);
        assertThat(isValid, is(true));
    }

    private String getVehicleCodedId() {
        return chassisSeries + VEHICLE_ID_SPLITTER + chassisNumber;
    }

    @Test
    public void failToDecodingVehicleIdWhenIdIsNull() {
        boolean isValid = vehicleIdValidator.isValid(null, null);
        assertThat(isValid, is(false));
    }

    @Test
    public void failToDecodingVehicleIdWhenIdIsEmpty() {
        boolean isValid = vehicleIdValidator.isValid("", null);
        assertThat(isValid, is(false));
    }

    @Test
    public void failToDecodingVehicleIdWhenChassisSeriesIsEmpty() {
        chassisSeries = "";
        chassisNumber = "123";

        boolean isValid = vehicleIdValidator.isValid(getVehicleCodedId(), null);
        assertThat(isValid, is(false));
    }

    @Test
    public void failToDecodingVehicleIdWhenChassisNumberIsEmpty() {
        chassisSeries = "ABC";
        chassisNumber = "";

        boolean isValid = vehicleIdValidator.isValid(getVehicleCodedId(), null);
        assertThat(isValid, is(false));
    }

    @Test
    public void failToDecodingVehicleIdWhenChassisNumberIsNotNumeric() {
        chassisSeries = "ABC";
        chassisNumber = "EFG";

        boolean isValid = vehicleIdValidator.isValid(getVehicleCodedId(), null);
        assertThat(isValid, is(false));
    }
}
