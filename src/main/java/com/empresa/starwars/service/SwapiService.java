package com.empresa.starwars.service;

import com.empresa.starwars.clients.StarWarsClient;
import com.empresa.starwars.domain.SwapiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Builder
public class SwapiService {
    private final StarWarsClient starWarsClient;

    @Cacheable(cacheNames = "planet", key = "#planet")
    public SwapiResponse getPlanets(String planet){
        return starWarsClient.getPlanets(planet);
    }

}
