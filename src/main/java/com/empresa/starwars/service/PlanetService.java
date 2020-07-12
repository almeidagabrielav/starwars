package com.empresa.starwars.service;

import com.empresa.starwars.clients.StarWarsClient;
import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.domain.PlanetDTO;
import com.empresa.starwars.domain.PlanetSwapiResponse;
import com.empresa.starwars.domain.SwapiResponse;
import com.empresa.starwars.repository.PlanetRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Builder
public class PlanetService {

    //region Propriedades
    private final PlanetRepository planetRepository;
    private final SwapiService swapiService;
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

    //region Métodos Públicos
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
            List<PlanetDTO> planetsDTO = new ArrayList<PlanetDTO>();
            List<Planet> planets = planetRepository.findAll();
            for(Planet planet: planets){
                PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
                SwapiResponse response = swapiService.getPlanets(planet.getName());
                if(true){
                    throw GenericApiException.builder()
                            .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                            .statusCode(HttpStatus.BAD_REQUEST)
                            .message("Não foi possível obter a quantidade de aparições em filmes pois o método retornou mais de um resultado na busca por nome.")
                            .build();
                }
                else {
                    planetDTO.setCount_films_appearances(response.getResults().get(0).getFilms().size());
                }

                planetsDTO.add(planetDTO);
            }
            return planetsDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error when find planets")
                    .build();
        }
    }

    public PlanetDTO findById(String id){
        try{
            Planet planet = planetRepository.findById(id).get();
            SwapiResponse response = swapiService.getPlanets(planet.getName());
            PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);

            if(response.getCount() > 1){
                throw new Exception("Não foi possível obter a quantidade de aparições em filmes pois o método retornou mais de um resultado na busca por nome.");
            }
            else {
                planetDTO.setCount_films_appearances(response.getResults().get(0).getFilms().size());
            }
           return planetDTO;
        }
        catch (Exception ex){
            return null;
        }
    }

    public PlanetDTO findByName(String name){
        try{
            Planet planet = planetRepository.findByName(name);
            SwapiResponse response = swapiService.getPlanets(planet.getName());
            PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);

            if(response.getCount() > 1){
                throw new Exception("Não foi possível obter a quantidade de aparições em filmes pois o método retornou mais de um resultado na busca por nome.");
            }
            else {
                planetDTO.setCount_films_appearances(response.getResults().get(0).getFilms().size());
            }

            return planetDTO;
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
