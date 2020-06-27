package com.empresa.starwars.interfaces;

import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPlanet(@RequestBody Planet newPlanet){
        List<Planet> planets  = planetService.createPlanet(newPlanet);

        return Optional.ofNullable(planets)
                .map(x -> ResponseEntity.ok().body(planets))
                .orElse(ResponseEntity.status(418).build());
    }

    @PutMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePlanet(@PathVariable(value = "id") int id, @RequestBody Planet planetDetails){
        List<Planet> planets  = planetService.updatePlanet(id,planetDetails);

        return Optional.ofNullable(planets)
                .map(x -> ResponseEntity.ok().body(planets))
                .orElse(ResponseEntity.status(418).build());
    }

    @DeleteMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deletePlanet(@PathVariable(value = "id") int id){

        return Optional.ofNullable(id)
                .map(x -> ResponseEntity.ok().body(id))
                .orElse(ResponseEntity.status(418).build());
    }

    @GetMapping( value = "/planets", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@RequestParam int id){
        Planet planet  = planetService.getById(id);

        return Optional.ofNullable(planet)
                .map(x -> ResponseEntity.ok().body(planet))
                .orElse(ResponseEntity.status(418).build());
    }


    @GetMapping( value = "/planets", params="name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getByName(@RequestParam String name){
        Planet planet  = planetService.getByName(name);

        return Optional.ofNullable(planet)
                .map(x -> ResponseEntity.ok().body(planet))
                .orElse(ResponseEntity.status(418).build());
    }
}
