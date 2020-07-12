package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Builder
public class PlanetSwapiResponse implements Serializable {
    private String name;
    private String terrain;
    private String climate;
    private ArrayList<String> films;
}
