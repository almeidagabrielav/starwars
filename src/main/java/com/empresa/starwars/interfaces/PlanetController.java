package com.empresa.starwars.interfaces;

import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.domain.PlanetDTO;
import com.empresa.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PlanetController {

    private final PlanetService planetService;

    @GetMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll(){
        List<PlanetDTO> planetsDTO  = planetService.findAll();

        return Optional.ofNullable(planetsDTO)
                .map(x -> ResponseEntity.ok().body(planetsDTO))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping( value = "/planets", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@RequestParam String id){
        PlanetDTO planetDTO  = planetService.findById(id);

        return Optional.ofNullable(planetDTO)
                .map(x -> ResponseEntity.status(HttpStatus.CREATED).body(planetDTO))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }


    @GetMapping( value = "/planets", params="name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getByName(@RequestParam String name){
        PlanetDTO planetDTO  = planetService.findByName(name);

        return Optional.ofNullable(planetDTO)
                .map(x -> ResponseEntity.ok().body(planetDTO))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlanetDTO> savePlanet(@RequestBody @Valid PlanetDTO newPlanet){
        PlanetDTO planetDTO  = planetService.savePlanet(newPlanet);

        return new ResponseEntity<>(planetDTO, HttpStatus.CREATED);
//        return Optional.ofNullable(planetDTO)
//                .map(x -> ResponseEntity.ok().body(planetDTO))
//                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePlanet(@PathVariable(value = "id") String id, @RequestBody PlanetDTO planetDTO){
        planetService.updatePlanet(id, planetDTO);
    }

    @DeleteMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deletePlanet(@PathVariable(value = "id") String id){
        planetService.deletePlanet(id);
    }
}
