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

import java.util.ArrayList;
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
            List<PlanetDTO> planetsDTO  = planetService.findAll();
            return Optional.ofNullable(planetsDTO)
                    .map(x -> ResponseEntity.ok().body(planetsDTO))
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
            PlanetDTO planetDTO  = planetService.findById(id);

            return Optional.ofNullable(planetDTO)
                    .map(x -> ResponseEntity.ok().body(planetDTO))
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
            PlanetDTO planetDTO  = planetService.findByName(name);

            return Optional.ofNullable(planetDTO)
                    .map(x -> ResponseEntity.ok().body(planetDTO))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (GenericApiException ex){
            log.error("Error getting planets by name ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

    }

    @PostMapping( value = "/planets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity savePlanet(@RequestBody PlanetDTO newPlanet){

        try{
            checkPostValidation(newPlanet);
            PlanetDTO planetDTO = planetService.savePlanet(newPlanet);
            return Optional.ofNullable(planetDTO)
                    .map(x -> ResponseEntity.status(HttpStatus.CREATED).build())
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
        catch (GenericApiException ex){
            log.error("Error saving planet ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }

    }

    @PutMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePlanet(@PathVariable(value = "id") String id, @RequestBody PlanetDTO updatedPlanet){
        try{
            PlanetDTO planetDTO = planetService.updatePlanet(id, updatedPlanet);

            return Optional.ofNullable(planetDTO)
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

    //region Private Methods

    private List<String> checkInvalidFields(PlanetDTO planetDTO){
        List<String> fields = new ArrayList<String>();

        if(planetDTO.getName() == null || planetDTO.getName().isEmpty()){
            fields.add("name");
        }
        if(planetDTO.getClimate() == null || planetDTO.getClimate().isEmpty()){
            fields.add("climate");
        }
        if(planetDTO.getTerrain() == null || planetDTO.getTerrain().isEmpty()){
            fields.add("terrain");
        }

        return fields;
    }

    private void checkPostValidation(PlanetDTO planetDTO){
        List<String> fields = checkInvalidFields(planetDTO);

        if(!fields.isEmpty() || fields.size() > 0){
            String message = "";
            if(fields.size() == 1){
                message = "Property " + fields.get(0) + " is required.";
            }
            else {
                String fieldsValues = "";
                for(String s : fields){
                    fieldsValues += s + ", ";
                }
                fieldsValues = fieldsValues.substring(0, fieldsValues.length() - 2);
                message = "Properties " + fieldsValues + " are required.";
            }

            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message(message)
                    .build();
        }
    }

    //endregion
}
