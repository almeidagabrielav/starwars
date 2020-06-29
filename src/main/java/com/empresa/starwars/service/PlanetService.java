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

    private final PlanetRepository planetRepository;

    public Planet savePlanet(Planet planet){
        return planetRepository.save(planet);
    }

    public List<Planet> findAll(){
        return planetRepository.findAll();
    }

//    private List<Planet> fillGlobalPlanets(){
//        List<Planet> globalPlanets = new ArrayList<>();
//        Planet planet = Planet.builder()
//                .Id(1)
//                .Name("Coruscant")
//                .Clime("Tropical")
//                .Ground("Plano")
//                .build();
//
//        Planet planet1 = Planet.builder()
//                .Id(2)
//                .Name("Naboo")
//                .Clime("Tropical")
//                .Ground("Plano")
//                .build();
//
//        Planet planet2 = Planet.builder()
//                .Id(3)
//                .Name("Estrela da Morte")
//                .Clime("Tropical")
//                .Ground("Plano")
//                .build();
//
//        globalPlanets.add(planet);
//        globalPlanets.add(planet1);
//        globalPlanets.add(planet2);
//
//        return globalPlanets;
//    }
//
//    public List<Planet> getAll(){
//        return fillGlobalPlanets();
//    }
//
//    public Planet getById(int id){
//
//        for (Planet planet: fillGlobalPlanets()){
//            if(planet.getId() == id)
//                return planet;
//        }
//
//        return null;
//    }
//
//    public Planet getByName(String name){
//
//        for (Planet planet: fillGlobalPlanets()){
//            if(planet.getName().equals(name))
//                return planet;
//        }
//
//        return null;
//    }
//
//    public List<Planet> createPlanet(Planet newPlanet){
//        List<Planet> planets = fillGlobalPlanets();
//        Planet planet = Planet.builder()
//                .Id(10)
//                .Name(newPlanet.getName())
//                .Clime(newPlanet.getClime())
//                .Ground(newPlanet.getGround())
//                .build();
//
//        planets.add(planet);
//        return planets;
//    }
//
//    public List<Planet> updatePlanet(int id, Planet planetDetails){
//        List<Planet> planets = fillGlobalPlanets();
//
//        for(Planet planet: planets){
//            if(planet.getId() == id){
//                planet.setName(planetDetails.getName());
//                planet.setClime(planetDetails.getClime());
//                planet.setGround(planetDetails.getGround());
//            }
//        }
//
//        return planets;
//    }
//
//    public List<Planet> deletePlanet(int id){
//        List<Planet> planets = fillGlobalPlanets();
//
//        for(Planet planet: planets){
//            if(planet.getId() == id){
//                planets.remove(planet);
//            }
//        }
//
//        return planets;
//    }
}
