package com.empresa.starwars.domain;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class PlanetRequest {
    PlanetRequest(){}

    PlanetRequest(String name, String climate, String terrain){
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    @NotNull (message = "Property name is required.")
    @NotEmpty (message = "Property name is required.")
    private String name;
    @NotNull (message = "Property climate is required.")
    @NotEmpty (message = "Property climate is required.")
    private String climate;
    @NotNull (message = "Property terrain is required.")
    @NotEmpty (message = "Property terrain is required.")
    private String terrain;
}
