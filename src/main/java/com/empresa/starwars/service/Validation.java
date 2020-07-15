package com.empresa.starwars.service;

import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.domain.PlanetDTO;
import com.empresa.starwars.domain.PlanetSwapiResponse;
import com.empresa.starwars.domain.SwapiResponse;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class Validation {

    //region Public Methods
    public void checkPlanetExistence(SwapiResponse response, PlanetDTO planetDTO){
        if(response == null){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It is not possible to register a planet with that name because it is not in the public api of Star Wars.")
                    .build();
        }

        checkResponseSize(response.getResults().size());

        if(response.getResults().get(0).getName().equals(planetDTO.getName())) {
            PlanetSwapiResponse planetSwapiResponse =  response.getResults().get(0);
            checkEqualClimate(planetDTO.getClimate(), planetSwapiResponse.getClimate());
            checkEqualTerrain(planetDTO.getTerrain(), planetSwapiResponse.getTerrain());
        }
        else{
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It is not possible to register a planet with that name because it is not in the public api of Star Wars.")
                    .build();
        }
    }

    public void checkPlanetId(Optional<Planet> planet){
        if(planet.isEmpty() || planet == null){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with that id.")
                    .build();
        }
    }

    public void checkPlanetName(Planet planet, boolean isPost){
        if(planet == null && !isPost){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with that name.")
                    .build();
        }
        else if(isPost)
        {
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is already a registered planet with the same name.")
                    .build();
        }
    }

    public void checkGetValidation(SwapiResponse response){
        if(response == null){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with that name.")
                    .build();
        }
        if(response.getResults().size() > 1){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search.")
                    .build();
        }
    }
    //endregion

    //region Private Methods

    private void checkResponseSize(int size){
        if(size > 1){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search.")
                    .build();
        }
    }

    private void checkEqualClimate(String planetDTOClimate, String planetSwapiClimate){
        if(!planetSwapiClimate.equals(planetDTOClimate)){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("The reported climate does not agree with the climate found for this planet in the public api of Star Wars.")
                    .build();
        }
    }

    private void checkEqualTerrain(String planetDTOTerrain, String planetSwapiTerrain){
        if(!planetSwapiTerrain.equals(planetDTOTerrain)){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("The reported terrain does not agree with the terrain found for this planet in the public api of Star Wars.")
                    .build();
        }
    }

    //endregion
}
