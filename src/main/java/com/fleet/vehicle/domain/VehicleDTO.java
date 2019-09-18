package com.fleet.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VehicleDTO {

    private String chassisSeries;
    private Integer chassisNumber;
    private String type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte passengersNumber;
    private String color;
}
