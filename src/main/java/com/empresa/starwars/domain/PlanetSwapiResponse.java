package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class PlanetSwapiResponse {

    private String name;
    private ArrayList<String> films;
}
