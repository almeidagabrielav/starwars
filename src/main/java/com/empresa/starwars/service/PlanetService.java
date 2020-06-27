package com.empresa.starwars.service;

import com.empresa.starwars.domain.Planet;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Builder
public class PlanetService {

    public List<Planet> getAll(){
        Planet planet = Planet.builder()
                .Id(1)
                .Name("Coruscant")
                .Clime("Tropical")
                .Ground("Plano")
                .build();

        Planet planet1 = Planet.builder()
                .Id(2)
                .Name("Naboo")
                .Clime("Tropical")
                .Ground("Plano")
                .build();

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(planet);
        planets.add(planet1);

        return planets;
    }

    public Planet getById(int id){
        Planet planet = Planet.builder()
                .Id(id)
                .Name("Coruscant")
                .Clime("Tropical")
                .Ground("Plano")
                .build();

        return planet;
    }

    public List<Planet> getByName(String name){
        Planet planet = Planet.builder()
                .Id(2)
                .Name(name)
                .Clime("Tropical")
                .Ground("Plano")
                .build();

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(planet);

        return planets;
    }
}
