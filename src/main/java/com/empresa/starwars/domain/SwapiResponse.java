package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Builder
public class SwapiResponse implements Serializable {

    private int count;
    private ArrayList<PlanetSwapiResponse> results;
}
