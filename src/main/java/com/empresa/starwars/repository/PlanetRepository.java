package com.empresa.starwars.repository;

import com.empresa.starwars.domain.Planet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlanetRepository extends MongoRepository<Planet, String> {

    Planet findByName(final String name);
}
