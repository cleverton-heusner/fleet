package com.fleet.vehicle;

import com.fleet.vehicle.VehicleDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleDomain, VehicleDomain.VehicleId> {
}
