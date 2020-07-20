package com.empresa.starwars.repository;

import com.empresa.starwars.domain.Planet;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            Optional<Planet> planetOptional = planetRepository.findById(id);
            return planetOptional.orElse(null);
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
            throw ex;
        }
    }
    //endregion

}
