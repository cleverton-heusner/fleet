package com.fleet;

import com.fleet.vehicle.VehicleDomain;
import com.fleet.vehicle_type.VehicleType;
import com.fleet.vehicle.VehicleRepository;
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

    private VehicleDomain expectedVehicleDomain;

    @Before
    public void setUp() {
        expectedVehicleDomain = createExpectedVehicle();
    }

    private VehicleDomain createExpectedVehicle() {
        VehicleDomain.VehicleId vehicleId = VehicleDomain.VehicleId.builder()
                .chassisNumber(123)
                .chassisSeries("ABC")
                .build();
        return VehicleDomain.builder()
                .id(vehicleId)
                .color("red")
                .passengersNumber((byte) 42)
                .type(VehicleType.BUS)
                .build();
    }

    @Test
    public void insertVehicleWhenDetailsIsValid() {
        vehicleRepository.save(expectedVehicleDomain);

        VehicleDomain realVehicleDomain = vehicleRepository.findById(expectedVehicleDomain.getId()).get();
        assertThat(realVehicleDomain, is(equalTo(expectedVehicleDomain)));
    }

    @Test
    public void findVehicleWhenThereIsVehicle() {
        populateDatabase();

        VehicleDomain realVehicleDomain = vehicleRepository.findById(expectedVehicleDomain.getId()).get();

        assertThat(realVehicleDomain, is(equalTo(expectedVehicleDomain)));
    }

    private void populateDatabase() {
        entityManager.persist(expectedVehicleDomain);
        entityManager.flush();
    }

    @Test
    public void listVehiclesWhenThereAreVehicles() {
        populateDatabase();

        List<VehicleDomain> realVehicleDomains = vehicleRepository.findAll();

        List<VehicleDomain> expectedVehiclesDomain = Arrays.asList(expectedVehicleDomain);
        assertThat(realVehicleDomains, is(equalTo(expectedVehiclesDomain)));
    }

    @Test
    public void changeVehicleColorWhenThereIsVehicle() {
        populateDatabase();

        VehicleDomain vehicleDomain = vehicleRepository.findById(expectedVehicleDomain.getId()).get();
        vehicleDomain.setColor(NEW_COLOR);
        vehicleRepository.save(vehicleDomain);

        VehicleDomain vehicleDomainWithNewColor = vehicleRepository.findById(expectedVehicleDomain.getId()).get();
        assertThat(vehicleDomainWithNewColor.getColor(), is(equalTo(NEW_COLOR)));
    }

    @Test
    public void removeVehicleByIdWhenThereIsVehicle() {
        populateDatabase();

        vehicleRepository.deleteById(expectedVehicleDomain.getId());

        Optional<VehicleDomain> optionalVehicle = vehicleRepository.findById(expectedVehicleDomain.getId());
        assertThat(optionalVehicle.isPresent(), is(false));
    }
}
