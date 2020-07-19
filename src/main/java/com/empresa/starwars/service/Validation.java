package com.empresa.starwars.service;

import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@RequiredArgsConstructor
@Builder
@Configuration
public class Validation {

    //region Public Methods
    //ok
    public void checkSwapiPlanetExistence(SwapiResponse response, PlanetRequest request){
        if(response == null){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It is not possible to register a planet with that name because it is not in the public api of Star Wars.")
                    .build();
        }

        checkResponseSize(response.getCount());

        if(!response.getResults().get(0).getName().equals(request.getName())) {
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It is not possible to register a planet with that name because it is not in the public api of Star Wars.")
                    .build();
        }
    }
    //optional
    public void checkPlanetId(Optional<Planet> planet){
        if(planet.isEmpty() || planet == null){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with that id.")
                    .build();
        }
    }

    //ok
    public void checkPlanetName(Planet planet, boolean isGet){
        if(planet == null && isGet){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with that name.")
                    .build();
        }
        else if(planet != null && !isGet)
        {
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is already a registered planet with the same name.")
                    .build();
        }
    }

    //ok
    public void checkPlanetNameAndId(Planet planet, String id){
        if(!planet.getId().equals(id)){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is already a registered planet with the same name with a different id.")
                    .build();
        }
    }

    //ok
    public void checkGetValidation(SwapiResponse response){
        if(response == null){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with that name.")
                    .build();
        }
        checkResponseSize(response.getCount());
    }
    //endregion

    //region Private Methods

    private void checkResponseSize(int size){
        if(size == 0){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("There is no planet with the data reported.")
                    .build();
        }
        if(size > 1){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search.")
                    .build();
        }
    }

    //endregion
}
