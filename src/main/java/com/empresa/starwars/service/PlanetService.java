package com.empresa.starwars.service;

import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.domain.PlanetDTO;
import com.empresa.starwars.repository.PlanetRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Builder
public class PlanetService {

    //region Propriedades
    private final PlanetRepository planetRepository;
    //endregion

    //region Métodos Privados

    private PlanetDTO convertPlanetToPlanetDTO(Planet planet){

        return PlanetDTO.builder()
                .id(planet.getId())
                .name(planet.getName())
                .climate(planet.getClimate())
                .terrain(planet.getTerrain())
                .build();
    }

    private Planet convertPlanetDTOToPlanet(PlanetDTO planetDTO){

        return Planet.builder()
                .name(planetDTO.getName())
                .climate(planetDTO.getClimate())
                .terrain(planetDTO.getTerrain())
                .build();
    }

    //endregion

    //region Métodos API
    public PlanetDTO savePlanet(PlanetDTO planetDTO){
        try{
            Planet planet = convertPlanetDTOToPlanet(planetDTO);
            planetRepository.save(planet);
            planetDTO = convertPlanetToPlanetDTO(planet);
            return planetDTO;
        }
        catch (Exception ex){
            return null;
        }
    }

    public List<PlanetDTO> findAll(){
        try {
            List<PlanetDTO> planetsDTO = new ArrayList<>();
            for(Planet planet: planetRepository.findAll()){
                PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
                planetsDTO.add(planetDTO);
            }
            return planetsDTO;
        }
        catch (Exception ex){
            return null;
        }
    }

    public PlanetDTO findById(String id){
        try{
            Planet planet = planetRepository.findById(id).get();
            return convertPlanetToPlanetDTO(planet);
        }
        catch (Exception ex){
            return null;
        }
    }

    public PlanetDTO findByName(String name){
        try{
            Planet planet = planetRepository.findByName(name);
            return convertPlanetToPlanetDTO(planet);
        }
        catch (Exception ex){
            return null;
        }
    }

    public void updatePlanet(String id, PlanetDTO planetDTO){
        try{
            Planet planet = convertPlanetDTOToPlanet(planetDTO);
            planet.setId(id);
            planetRepository.save(planet);
        }
        catch (Exception ex){
        }
    }

    public void deletePlanet(String id){
        try{
            planetRepository.deleteById(id);
        }
        catch (Exception ex){

        }
    }
    //endregion



}
