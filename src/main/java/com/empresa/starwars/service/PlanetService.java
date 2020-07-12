package com.empresa.starwars.service;

import com.empresa.starwars.clients.StarWarsClient;
import com.empresa.starwars.configuration.exceptions.GenericApiException;
import com.empresa.starwars.domain.Planet;
import com.empresa.starwars.domain.PlanetDTO;
import com.empresa.starwars.domain.PlanetSwapiResponse;
import com.empresa.starwars.domain.SwapiResponse;
import com.empresa.starwars.repository.PlanetRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Builder
public class PlanetService {

    //region Properties
    private final PlanetRepository planetRepository;
    private final SwapiService swapiService;
    //endregion

    //region Private Methods

    private PlanetDTO convertPlanetToPlanetDTO(Planet planet){

        return PlanetDTO.builder()
                .id(planet.getId())
                .name(planet.getName())
                .climate(planet.getClimate())
                .terrain(planet.getTerrain())
                .build();
    }

    private Planet convertPlanetDTOToPlanet(PlanetDTO planetDTO){

        return Planet.builder()
                .name(planetDTO.getName())
                .climate(planetDTO.getClimate())
                .terrain(planetDTO.getTerrain())
                .build();
    }
    private void isIdValid(String id){
        try {
            Planet planet = planetRepository.findById(id).get();
            if(planet == null){
                throw GenericApiException.builder()
                        .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message("There is no planet with that id.")
                        .build();
            }
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by id")
                    .build();
        }
    }

    private void isValid(PlanetDTO planetDTO){
        try{
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

            SwapiResponse response = swapiService.getPlanets(planetDTO.getName());
            if(response == null){
                throw GenericApiException.builder()
                        .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message("It is not possible to register a planet with that name because it is not in the public api of Star Wars.")
                        .build();
            }
            if(response.getResults().size() > 1){
                throw GenericApiException.builder()
                        .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message("It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search.")
                        .build();
            }
            else {
                PlanetSwapiResponse planetSwapiResponse =  response.getResults().get(0);
                if(!planetSwapiResponse.getClimate().equals(planetDTO.getClimate())){
                    throw GenericApiException.builder()
                            .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                            .statusCode(HttpStatus.BAD_REQUEST)
                            .message("The reported climate does not agree with the climate found for this planet in the public api of Star Wars.")
                            .build();
                }
                if(!planetSwapiResponse.getTerrain().equals(planetDTO.getTerrain())){
                    throw GenericApiException.builder()
                            .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                            .statusCode(HttpStatus.BAD_REQUEST)
                            .message("The reported terrain does not agree with the climate found for this planet in the public api of Star Wars.")
                            .build();
                }
            }
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error saving planets")
                    .build();
        }
    }

    private SwapiResponse isGetValid(PlanetDTO planetDTO){
        try {
            SwapiResponse response = swapiService.getPlanets(planetDTO.getName());
            if(response == null){
                throw GenericApiException.builder()
                        .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message("There is no planet with that name.")
                        .build();
            }
            if(response.getResults().size() > 1){
                throw GenericApiException.builder()
                        .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message("It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search.")
                        .build();
            }
            return response;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets")
                    .build();
        }
    }

    //endregion

    //region Public Methods
    public PlanetDTO savePlanet(PlanetDTO planetDTO){
        try{
            isValid(planetDTO);
            Planet planet = convertPlanetDTOToPlanet(planetDTO);
            planetRepository.save(planet);
            planetDTO = convertPlanetToPlanetDTO(planet);
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error saving planets")
                    .build();
        }
    }

    public List<PlanetDTO> findAll(){
        try {
            List<PlanetDTO> planetsDTO = new ArrayList<PlanetDTO>();
            List<Planet> planets = planetRepository.findAll();
            for(Planet planet: planets){
                PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
                SwapiResponse response = isGetValid(planetDTO);
                planetDTO.setCountFilmsAppearances(response.getResults().get(0).getFilms().size());
                planetsDTO.add(planetDTO);
            }
            return planetsDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets")
                    .build();
        }
    }

    public PlanetDTO findById(String id){
        try{
            Planet planet = planetRepository.findById(id).get();
            PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
            SwapiResponse response = isGetValid(planetDTO);
            planetDTO.setCountFilmsAppearances(response.getResults().get(0).getFilms().size());
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by id")
                    .build();
        }
    }

    public PlanetDTO findByName(String name){
        try{
            Planet planet = planetRepository.findByName(name);
            PlanetDTO planetDTO = convertPlanetToPlanetDTO(planet);
            SwapiResponse response = isGetValid(planetDTO);
            planetDTO.setCountFilmsAppearances(response.getResults().get(0).getFilms().size());
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error getting planets by name")
                    .build();
        }
    }

    public PlanetDTO updatePlanet(String id, PlanetDTO planetDTO){
        try{
            isIdValid(id);
            isValid(planetDTO);
            Planet planet = convertPlanetDTOToPlanet(planetDTO);
            planet.setId(id);
            planetRepository.save(planet);
            planetDTO = convertPlanetToPlanetDTO(planet);
            return planetDTO;
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error updating planets")
                    .build();
        }
    }

    public void deletePlanet(String id){
        try{
            isIdValid(id);
            planetRepository.deleteById(id);
        }
        catch (GenericApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw GenericApiException.builder()
                    .code(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error deleting planets")
                    .build();
        }
    }
    //endregion

}
