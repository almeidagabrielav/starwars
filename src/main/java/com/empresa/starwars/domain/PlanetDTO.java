package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
public class PlanetDTO {

    private String id;
    @NotBlank(message = "{{name.not.blank}}")
    private String name;
    @NotBlank(message = "{{climate.not.blank}}")
    private String climate;
    @NotBlank(message = "{{terrain.not.blank}}")
    private String terrain;
    private Integer count_films_appearances;

}
