package com.empresa.starwars.clients;

import com.empresa.starwars.domain.SwapiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name ="StarWarsAPI", url = "${integration.swapi:https://swapi.dev/api}")
public interface StarWarsClient {

    @GetMapping(value="/planets/")
    SwapiResponse getPlanets(@RequestParam("search") String planet);
}
