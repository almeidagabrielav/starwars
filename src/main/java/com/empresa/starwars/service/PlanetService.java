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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Builder
public class PlanetService {

    //region Properties
    private final PlanetRepository planetRepository;
    private final SwapiService swapiService;
    private final Validation validation;
    //endregion

    //region Public Methods
    public PlanetDTO savePlanet(PlanetDTO planetDTO){
        try{
            checkPlanetName(planetDTO.getName(), true);
            checkPlanetExistence(planetDTO);
            Planet planet = convertPlanetDTOToPlanet(planetDTO);
            planetRepository.save(planet);
            planetDTO = convertPlanetToPlanetDTO(planet);
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error saving planets")
                    .build();
        }
    }

    public List<PlanetDTO> findAll(){
        try {
            List<PlanetDTO> planetsDTO = new ArrayList<PlanetDTO>();
            List<Planet> planets = planetRepository.findAll();
            for(Planet planet: planets){
                PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
                SwapiResponse response = checkGetValidation(planetDTO);
                planetDTO.setCountFilmsAppearances(response.getResults().get(0).getFilms().size());
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
                    .message("Error getting planets")
                    .build();
        }
    }

    @Cacheable(value = "planetDTO", key = "#id", unless = "#result == null")
    public PlanetDTO findById(String id){
        try{
            checkPlanetId(id);
            Planet planet = planetRepository.findById(id).get();
            PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
            SwapiResponse response = checkGetValidation(planetDTO);
            planetDTO.setCountFilmsAppearances(response.getResults().get(0).getFilms().size());
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by id")
                    .build();
        }
    }

    @Cacheable(value = "planetDTO", key = "#name", unless = "#result == null")
    public PlanetDTO findByName(String name){
        try{
            checkPlanetName(name, false);
            Planet planet = planetRepository.findByName(name);
            PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
            SwapiResponse response = checkGetValidation(planetDTO);
            planetDTO.setCountFilmsAppearances(response.getResults().get(0).getFilms().size());
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by name")
                    .build();
        }
    }

    @CachePut(value = "planetDTO", key = "#id")
    public PlanetDTO updatePlanet(String id, PlanetDTO planetDTO){
        try{
            checkPlanetId(id);
            checkPlanetExistence(planetDTO);
            Planet planet = convertPlanetDTOToPlanet(planetDTO);
            planet.setId(id);
            planetRepository.save(planet);
            planetDTO = convertPlanetToPlanetDTO(planet);
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error updating planets")
                    .build();
        }
    }

    @CacheEvict(value = "planetDTO", key = "#id")
    public void deletePlanet(String id){
        try{
            checkPlanetId(id);
            planetRepository.deleteById(id);
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error deleting planets")
                    .build();
        }
    }

    //endregion

    //region Private Methods

    //region Mapping
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

    //region Validation

    private void checkPlanetExistence(PlanetDTO planetDTO){
        try{
            SwapiResponse response = swapiService.getPlanets(planetDTO.getName());
            validation.checkPlanetExistence(response, planetDTO);
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error saving planets")
                    .build();
        }
    }

    private void checkPlanetId(String id){
        try {
            Optional<Planet> planet = planetRepository.findById(id);
            validation.checkPlanetId(planet);
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by id")
                    .build();
        }
    }

    private void checkPlanetName(String name, boolean isPost){
        try {
            Planet planet = planetRepository.findByName(name);
            validation.checkPlanetName(planet, isPost);
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by name")
                    .build();
        }
    }

    private SwapiResponse checkGetValidation(PlanetDTO planetDTO){
        try {
            SwapiResponse response = swapiService.getPlanets(planetDTO.getName());
            validation.checkGetValidation(response);
            return response;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets")
                    .build();
        }
    }
    //endregion

    //endregion

}
