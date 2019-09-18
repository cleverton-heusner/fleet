package com.fleet;

import com.fleet.vehicle.domain.Vehicle;
import com.fleet.vehicle_type.domain.VehicleType;
import com.fleet.vehicle.repository.VehicleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integration-tests")
public class VehicleIntegrationTest {

    private final String NEW_COLOR = "NEW COLOR";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle expectedVehicle;

    @Before
    public void setUp() {
        expectedVehicle = createExpectedVehicle();
    }

    private Vehicle createExpectedVehicle() {
        Vehicle.VehicleId vehicleId = Vehicle.VehicleId.builder()
                .chassisNumber(123)
                .chassisSeries("ABC")
                .build();
        return Vehicle.builder()
                .id(vehicleId)
                .color("red")
                .passengersNumber((byte) 42)
                .type(VehicleType.BUS)
                .build();
    }

    @Test
    public void insertVehicleWhenDetailsIsValid() {
        vehicleRepository.save(expectedVehicle);

        Vehicle realVehicle = vehicleRepository.findById(expectedVehicle.getId()).get();
        assertThat(realVehicle, is(equalTo(expectedVehicle)));
    }

    @Test
    public void findVehicleWhenThereIsVehicle() {
        populateDatabase();

        Vehicle realVehicle = vehicleRepository.findById(expectedVehicle.getId()).get();

        assertThat(realVehicle, is(equalTo(expectedVehicle)));
    }

    private void populateDatabase() {
        entityManager.persist(expectedVehicle);
        entityManager.flush();
    }

    @Test
    public void listVehiclesWhenThereAreVehicles() {
        populateDatabase();

        List<Vehicle> realVehicles = vehicleRepository.findAll();

        List<Vehicle> expectedVehicles = Arrays.asList(expectedVehicle);
        assertThat(realVehicles, is(equalTo(expectedVehicles)));
    }

    @Test
    public void changeVehicleColorWhenThereIsVehicle() {
        populateDatabase();

        Vehicle vehicle = vehicleRepository.findById(expectedVehicle.getId()).get();
        vehicle.setColor(NEW_COLOR);
        vehicleRepository.save(vehicle);

        Vehicle vehicleWithNewColor = vehicleRepository.findById(expectedVehicle.getId()).get();
        assertThat(vehicleWithNewColor.getColor(), is(equalTo(NEW_COLOR)));
    }

    @Test
    public void removeVehicleByIdWhenThereIsVehicle() {
        populateDatabase();

        vehicleRepository.deleteById(expectedVehicle.getId());

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(expectedVehicle.getId());
        assertThat(optionalVehicle.isPresent(), is(false));
    }
}
