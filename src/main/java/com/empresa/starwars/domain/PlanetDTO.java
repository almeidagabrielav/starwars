package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PlanetDTO implements Serializable {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    private Integer countFilmsAppearances;

}
