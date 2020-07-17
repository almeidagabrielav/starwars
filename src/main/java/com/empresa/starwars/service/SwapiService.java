package com.empresa.starwars.service;

import com.empresa.starwars.domain.SwapiResponse;
import com.empresa.starwars.repository.SwapiCache;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Builder
public class SwapiService {
    private final SwapiCache swapiCache;

    public SwapiResponse getPlanets(String planet){
        return swapiCache.getPlanets(planet);
    }

}
