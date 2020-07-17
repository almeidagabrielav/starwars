package com.empresa.starwars.service

import com.empresa.starwars.domain.Planet
import com.empresa.starwars.domain.PlanetRequest
import com.empresa.starwars.domain.PlanetResponse
import com.empresa.starwars.repository.PlanetCache
import com.empresa.starwars.repository.PlanetRepository
import spock.lang.Unroll

class PlanetServiceTest extends spock.lang.Specification {

    //region Properties
    PlanetRepository planetRepository = Mock()
    SwapiService swapiService = Mock()
    Validation validation = Mock()
    PlanetCache planetCache = Mock()
    def planetService = new PlanetService(planetRepository, swapiService, validation, planetCache)
    //endregion

    //region Private Methods Tests

    //region Mapping Tests

    @Unroll
    def "convertPlanetToPlanetResponse(Planet planet) - OK"() {

        given:

        def planet = Planet.builder()
                .id(id)
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        def responseExpected = PlanetResponse.builder()
                .id(id)
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        when:
        def planetTest = planetService.convertPlanetToPlanetResponse(planet)

        then:
        responseExpected == planetTest

        where:
        id      |  name     | climate     | terrain
        "id 1"  |  "name 1" | "climate 1" | "terrain 1"
        "id 2"  |  "name 2" | "climate 2" | "terrain 2"
        "id 3"  |  "name 3" | "climate 3" | "terrain 3"
        "id 4"  |  "name 4" | "climate 4" | "terrain 4"
    }

    @Unroll
    def "convertPlanetRequestToPlanet(PlanetRequest request) - OK"() {

        given:
        def request = PlanetRequest.builder()
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        def planetExpected = Planet.builder()
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        when:
        def planetTest = planetService.convertPlanetRequestToPlanet(request)

        then:
        planetExpected == planetTest

        where:
        name     | climate     | terrain
        "name 1" | "climate 1" | "terrain 1"
        "name 2" | "climate 2" | "terrain 2"
        "name 3" | "climate 3" | "terrain 3"
        "name 4" | "climate 4" | "terrain 4"
    }

    //endregion

    //region Validation Tests

    //endregion

    //endregion
}
