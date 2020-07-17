package com.empresa.starwars.service;

import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.*;
import com.empresa.starwars.repository.PlanetCache;
import com.empresa.starwars.repository.PlanetRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
    private final PlanetCache planetCache;
    //endregion

    //region Public Methods
    public PlanetResponse savePlanet(PlanetRequest request){
        try{
            checkPlanetName(request.getName(), false);
            checkPlanetExistence(request);
            Planet planet = convertPlanetRequestToPlanet(request);
            planetRepository.save(planet);
            PlanetResponse response = convertPlanetToPlanetResponse(planet);
            return response;
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

    public List<PlanetResponse> findAll(){
        try {
            List<PlanetResponse> responses = new ArrayList<PlanetResponse>();
            List<Planet> planets = planetRepository.findAll();
            for(Planet planet: planets){
                PlanetResponse response = convertPlanetToPlanetResponse(planet);
                SwapiResponse swapiResponse = checkGetValidation(response);
                response.setCountFilmsAppearances(swapiResponse.getResults().get(0).getFilms().size());
                responses.add(response);
            }
            return responses;
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

    public PlanetResponse findById(String id){
        try{
            checkPlanetId(id);
            Planet planet = planetCache.findById(id);
            PlanetResponse response = convertPlanetToPlanetResponse(planet);
            SwapiResponse swapiResponse = checkGetValidation(response);
            response.setCountFilmsAppearances(swapiResponse.getResults().get(0).getFilms().size());
            return response;
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

    public PlanetResponse findByName(String name){
        try{
            checkPlanetName(name, true);
            Planet planet = planetCache.findByName(name);
            PlanetResponse response = convertPlanetToPlanetResponse(planet);
            SwapiResponse swapiResponse = checkGetValidation(response);
            response.setCountFilmsAppearances(swapiResponse.getResults().get(0).getFilms().size());
            return response;
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

    public PlanetResponse updatePlanet(String id, PlanetRequest request){
        try{
            checkPlanetNameAndId(request.getName(), id);
            checkPlanetId(id);
            checkPlanetExistence(request);
            Planet planet = convertPlanetRequestToPlanet(request);
            planet.setId(id);
            planetRepository.save(planet);
            PlanetResponse response = convertPlanetToPlanetResponse(planet);
            return response;
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
    private PlanetResponse convertPlanetToPlanetResponse(Planet planet){

        return PlanetResponse.builder()
                .id(planet.getId())
                .name(planet.getName())
                .climate(planet.getClimate())
                .terrain(planet.getTerrain())
                .build();
    }

    private Planet convertPlanetRequestToPlanet(PlanetRequest request){

        return Planet.builder()
                .name(request.getName())
                .climate(request.getClimate())
                .terrain(request.getTerrain())
                .build();
    }
    //endregion

    //region Validation

    private void checkPlanetExistence(PlanetRequest request){
        try{
            SwapiResponse swapiResponse = swapiService.getPlanets(request.getName());
            validation.checkSwapiPlanetExistence(swapiResponse, request);
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

    private void checkPlanetNameAndId(String name, String id){
        try {
            Planet planet = planetRepository.findByName(name);
            validation.checkPlanetNameAndId(planet, id);
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

    private void checkPlanetName(String name, boolean isGet){
        try {
            Planet planet = planetRepository.findByName(name);
            validation.checkPlanetName(planet, isGet);
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

    private SwapiResponse checkGetValidation(PlanetResponse response){
        try {
            SwapiResponse swapiResponse = swapiService.getPlanets(response.getName());
            validation.checkGetValidation(swapiResponse);
            return swapiResponse;
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
