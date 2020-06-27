package com.empresa.starwars.interfaces;

import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PlanetController {

    //Regra injeção dependencia, procurar. Colocar final ou @Authorid
    private final PlanetService planetService;

    @GetMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(){
        List<Planet> planets  = planetService.getAll();

        return Optional.ofNullable(planets)
                .map(x -> ResponseEntity.ok().body(planets))
                .orElse(ResponseEntity.status(418).build());
    }

    @GetMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable(value = "id") int id){
        Planet planet  = planetService.getById(id);

        return Optional.ofNullable(planet)
                .map(x -> ResponseEntity.ok().body(planet))
                .orElse(ResponseEntity.status(418).build());
    }


    @GetMapping( value = "/planets/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getByName(@PathVariable(value = "name") String name){
        List<Planet> planets  = planetService.getByName(name);

        return Optional.ofNullable(planets)
                .map(x -> ResponseEntity.ok().body(planets))
                .orElse(ResponseEntity.status(418).build());
    }
}
