package com.empresa.starwars.domain;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SwapiResponse {

    private int count;
    private ArrayList<PlanetSwapiResponse> results;
}
