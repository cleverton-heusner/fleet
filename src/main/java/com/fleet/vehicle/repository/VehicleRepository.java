package com.fleet.vehicle.repository;

import com.fleet.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Vehicle.VehicleId> {
}
