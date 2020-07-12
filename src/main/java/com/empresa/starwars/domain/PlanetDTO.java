package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanetDTO {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    private Integer count_films_appearances;

}
