package com.fleet.vehicle;

import com.fleet.vehicle_type.VehicleType;
import com.fleet.vehicle_type.VehicleTypeConverter;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "vehicle")
public class VehicleDomain {

    @EmbeddedId
    private VehicleId id;

    @Convert(converter = VehicleTypeConverter.class)
    @Column(name = "type")
    private VehicleType type;

    @Column(name = "passengers_number")
    private byte passengersNumber;
    @Column(name = "color")
    private String color;

    @PrePersist
    public void setPassengersNumber() {
        this.passengersNumber = type.getPassengersNumber();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    @Embeddable
    public static class VehicleId implements Serializable {

        @Column(name = "chassis_series")
        private String chassisSeries;

        @Column(name = "chassis_number")
        private Integer chassisNumber;
    }
}
