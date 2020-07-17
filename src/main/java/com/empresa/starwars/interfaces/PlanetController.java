package com.empresa.starwars.interfaces;

import com.empresa.starwars.configuration.exceptions.ApiError;
import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.PlanetRequest;
import com.empresa.starwars.domain.PlanetResponse;
import com.empresa.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlanetController {

    //region Property
    private final PlanetService planetService;
    //endregion

    //region Public Methods
    @GetMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll(){
        try{
            List<PlanetResponse> responses  = planetService.findAll();
            return Optional.ofNullable(responses)
                    .map(x -> ResponseEntity.ok().body(responses))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (GenericApiException ex){
            log.error("Error getting planets ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

    }

    @GetMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findById(@PathVariable(value = "id") String id ){
        try{
            PlanetResponse response  = planetService.findById(id);

            return Optional.ofNullable(response)
                    .map(x -> ResponseEntity.ok().body(response))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (GenericApiException ex){
            log.error("Error getting planets by id ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }
    }

    @GetMapping( value = "/planets", params="name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findByName(@RequestParam String name){
        try{
            PlanetResponse response  = planetService.findByName(name);

            return Optional.ofNullable(response)
                    .map(x -> ResponseEntity.ok().body(response))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (GenericApiException ex){
            log.error("Error getting planets by name ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

    }

    @PostMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity savePlanet(@Valid @RequestBody PlanetRequest request){

        try{
            PlanetResponse response = planetService.savePlanet(request);
            return Optional.ofNullable(response)
                    .map(x -> ResponseEntity.status(HttpStatus.CREATED).build())
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
        catch (GenericApiException ex){
            log.error("Error saving planet ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

    }

    @PutMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePlanet(@PathVariable(value = "id") String id, @Valid @RequestBody PlanetRequest request){
        try{
            PlanetResponse response = planetService.updatePlanet(id, request);

            return Optional.ofNullable(response)
                    .map(x -> ResponseEntity.ok().build())
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
        catch (GenericApiException ex){
            log.error("Error updating planet ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }
    }

    @DeleteMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deletePlanet(@PathVariable(value = "id") String id){
        try{
            planetService.deletePlanet(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (GenericApiException ex){
            log.error("Error deleting planet ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }
    }
    //endregion

}
