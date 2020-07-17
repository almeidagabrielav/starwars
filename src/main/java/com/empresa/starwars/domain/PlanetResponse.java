package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PlanetResponse {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    private Integer countFilmsAppearances;
}
