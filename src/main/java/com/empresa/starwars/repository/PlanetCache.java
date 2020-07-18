package com.empresa.starwars.repository;

import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.Planet;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class PlanetCache {

    //region Properties
    private final PlanetRepository planetRepository;
    //endregion

    //region Public Methods
    @Cacheable(value = "planet", key = "#id", unless = "#result == null")
    public Planet findById(String id){
        try{
            Planet planet = planetRepository.findById(id).get();
            return planet;
        }
        catch (Exception ex){
            throw ex;
        }
    }

    @Cacheable(value = "planet", key = "#name", unless = "#result == null")
    public Planet findByName(String name){
        try{
            Planet planet = planetRepository.findByName(name);
            return planet;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by name")
                    .build();
        }
    }
    //endregion

}
