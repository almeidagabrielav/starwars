package com.empresa.starwars.repository;

import com.empresa.starwars.clients.StarWarsClient;
import com.empresa.starwars.domain.SwapiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class SwapiCache {

    private final StarWarsClient starWarsClient;

    @Cacheable(value = "swapiPlanet", key = "#planet", unless = "#result == null")
    public SwapiResponse getPlanets(String planet){
        return starWarsClient.getPlanets(planet);
    }
}
