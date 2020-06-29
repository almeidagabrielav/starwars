package com.empresa.starwars.service;

import com.empresa.starwars.domain.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlanetRepository extends MongoRepository<Planet, String> {

}
