package com.empresa.starwars.interfaces;

import com.empresa.starwars.configuration.exceptions.ApiError;
import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.PlanetRequest;
import com.empresa.starwars.domain.PlanetResponse;
import com.empresa.starwars.service.PlanetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@ApiOperation(value = "Operações relacionadas a planeta", response = PlanetController.class)
public class PlanetController {

    //region Property
    private final PlanetService planetService;
    //endregion

    //region Public Methods
    @ApiOperation(value = "Retorna uma lista de planetas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os planetas"),
            @ApiResponse(code = 404, message = "Nenhum planeta foi encontrado"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
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
        catch (Exception ex){
            log.error("Error getting planets ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @ApiOperation(value = "Busca um planeta pelo id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retonar o planeta associado ao id informado"),
            @ApiResponse(code = 404, message = "Nenhum planeta foi encontrado com o id informado"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
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
        catch (Exception ex){
            log.error("Error getting planets by id ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "Busca um planeta pelo nome")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retonar o planeta associado ao nome informado"),
            @ApiResponse(code = 404, message = "Nenhum planeta foi encontrado com o nome informado"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
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
        catch (Exception ex){
            log.error("Error getting planets by name ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @ApiOperation(value = "Salva um planeta na base de dados")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Um planeta foi criado com sucesso na base de dados"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
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
        catch (Exception ex){
            log.error("Error saving planet ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @ApiOperation(value = "Atualiza um planeta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Um planeta foi atualizado com sucesso"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
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
        catch (Exception ex){
            log.error("Error updating planet ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "Deleta um planeta pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Um planeta foi deletado com sucesso"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
    @DeleteMapping( value = "/planets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deletePlanet(@PathVariable(value = "id") String id){
        try{
            planetService.deletePlanet(id);
            return ResponseEntity.ok().build();
        }
        catch (GenericApiException ex){
            log.error("Error deleting planet ", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex));
        }
        catch (Exception ex){
            log.error("Error deleting planet ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //endregion

}
