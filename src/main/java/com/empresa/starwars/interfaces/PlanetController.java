package com.empresa.starwars.interfaces;

import com.empresa.starwars.configuration.exceptions.ApiError;
import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.PlanetDTO;
import com.empresa.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlanetController {

    private final PlanetService planetService;

    private void isValid(PlanetDTO planetDTO){
        if(planetDTO.getName() == null || planetDTO.getName().isEmpty()){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("Property name is required")
                    .build();
        }
        if(planetDTO.getClimate() == null || planetDTO.getClimate().isEmpty()){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("Property climate is required")
                    .build();
        }
        if(planetDTO.getTerrain() == null || planetDTO.getTerrain().isEmpty()){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("Property terrain is required")
                    .build();
        }
    }

    @GetMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll(){
        try{
            List<PlanetDTO> planetsDTO  = planetService.findAll();

            return Optional.ofNullable(planetsDTO)
                    .map(x -> ResponseEntity.ok().body(planetsDTO))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (GenericApiException ex){
            log.error("Error when get planets ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

    }

    @GetMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable(value = "id") String id ){
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
    public ResponseEntity savePlanet(@RequestBody PlanetDTO newPlanet){

        try{
            isValid(newPlanet);
            PlanetDTO planetDTO = planetService.savePlanet(newPlanet);

            return Optional.ofNullable(planetDTO)
                    .map(x -> ResponseEntity.status(HttpStatus.CREATED).build())
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
        catch (GenericApiException ex){
            log.error("Error when save planet ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

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
