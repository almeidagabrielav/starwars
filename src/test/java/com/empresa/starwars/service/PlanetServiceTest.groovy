package com.empresa.starwars.service

import com.empresa.starwars.clients.StarWarsClient
import com.empresa.starwars.configuration.exceptions.GenericApiException
import com.empresa.starwars.domain.Planet
import com.empresa.starwars.domain.PlanetRequest
import com.empresa.starwars.domain.PlanetResponse
import com.empresa.starwars.domain.SwapiResponse
import com.empresa.starwars.repository.PlanetCache
import com.empresa.starwars.repository.PlanetRepository
import com.empresa.starwars.repository.SwapiCache
import org.springframework.http.HttpStatus
import spock.lang.Unroll

class PlanetServiceTest extends spock.lang.Specification {

    //region Properties
    PlanetRepository planetRepository = Mock()
    StarWarsClient starWarsClient = Mock()
    Validation validation = Mock()
    def swapiCache = new SwapiCache(starWarsClient)
    def swapiService = new SwapiService(swapiCache)
    def planetCache = new PlanetCache(planetRepository)
    def planetService = new PlanetService(planetRepository, swapiService, validation, planetCache)

    //endregion

    //region Public Methods Tests
    @Unroll
    def "savePlanet(PlanetRequest request) - Success"() {
        given:

        def planetRequest = PlanetRequest.builder()
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        def planetResponseExpected = PlanetResponse.builder()
                .id(null)
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .countFilmsAppearances(null)
                .build()


        when:
        def planetResponse = planetService.savePlanet(planetRequest)

        then:
        planetResponse == planetResponseExpected

        where:
        name     | climate     | terrain
        "name 1" | "climate 1" | "terrain 1"
        "name 2" | "climate 2" | "terrain 2"
        "name 3" | "climate 3" | "terrain 3"
        "name 4" | "climate 4" | "terrain 4"
    }

    @Unroll
    def "savePlanet(PlanetRequest request)  - Error"() {
        given:
        def planetRequest = planetRequestMock

        when:
        planetService.savePlanet(planetRequest)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected         | planetRequestMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error saving planets"  | null

    }

    @Unroll
    def "updatePlanet(String id, PlanetRequest request) - Success"() {
        given:

        def id = idMock

        def planetRequest = PlanetRequest.builder()
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        def planetResponseExpected = PlanetResponse.builder()
                .id(id)
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .countFilmsAppearances(null)
                .build()


        when:
        def planetResponse = planetService.updatePlanet(id, planetRequest)

        then:
        planetResponse == planetResponseExpected

        where:
        idMock | name     | climate     | terrain
        "id 1" | "name 1" | "climate 1" | "terrain 1"
        "id 2" | "name 2" | "climate 2" | "terrain 2"
        "id 3" | "name 3" | "climate 3" | "terrain 3"
        "id 4" | "name 4" | "climate 4" | "terrain 4"
    }

    @Unroll
    def "updatePlanet(String id, PlanetRequest request)  - Error"() {
        given:
        def planetRequest = planetRequestMock
        def id = idMock

        when:
        planetService.updatePlanet(id, planetRequest)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected           | planetRequestMock   | idMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error updating planets"  | null                | null

    }

    @Unroll
    def "deletePlanet(String id) - Success"() {
        given:
        def id = idMock

        when:
        def result = planetService.deletePlanet(id)

        then:
        result == expected

        where:
        idMock | expected
        "id 1" | null
        "id 2" | null
        "id 3" | null
        "id 4" | null
    }

    @Unroll
    def "deletePlanet(String id)  - Error"() {
        given:
        def id = idMock

        when:
        planetService.deletePlanet(id)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected           | idMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error deleting planets"  | 1

    }

    @Unroll
    def "findAll() - Success"() {
        given:
        planetRepository.findAll() >> planetsMock
        planetService.checkGetValidation(Mock(PlanetResponse)) >> swapiResponseMock
        //swapiService.getPlanets(_ as String) >> swapiResponseMock

        when:
        def listplanetResponse = planetService.findAll()

        then:
        listplanetResponse == expected

        where:
        expected                                                                | planetsMock                                             | swapiResponseMock
        new ArrayList<PlanetResponse>(Arrays.asList(Mock(PlanetResponse)))      | new ArrayList<Planet>(Arrays.asList(Mock(Planet)))      | Mock(SwapiResponse)
    }

    @Unroll
    def "findAll()  - Error"() {

        when:
        planetService.findAll()

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets"

    }

    @Unroll
    def "findById(String id) - Success"() {
        given:

        planetCache.findById(id) >> planetMock
        planetService.checkGetValidation(Mock(PlanetResponse)) >> swapiResponseMock
        //swapiService.getPlanets(_ as String) >> swapiResponseMock

        when:
        def planetResponse = planetService.findById(id)

        then:
        planetResponse == planetResponseExpected

        where:
        id     | planetResponseExpected | planetMock   | swapiResponseMock
        "id 1" | Mock(PlanetResponse)   | Mock(Planet) | Mock(SwapiResponse)
    }

    @Unroll
    def "findById(String id)  - Error"() {
        given:
        def id = idMock

        when:
        planetService.findById(id)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected                | idMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by id"  | null

    }

    @Unroll
    def "findByName(String name) - Success"() {
        given:
        planetCache.findByName(name) >> planetMock
        planetService.checkGetValidation(Mock(PlanetResponse)) >> swapiResponseMock
        //swapiService.getPlanets(_ as String) >> swapiResponseMock

        when:
        def planetResponse = planetService.findByName(name)

        then:
        planetResponse == planetResponseExpected

        where:
        name     | planetMock   | swapiResponseMock    | planetResponseExpected
        "name 1" | Mock(Planet) | Mock(SwapiResponse)  | Mock(PlanetResponse)
    }

    @Unroll
    def "findByName(String name)  - Error"() {
        given:
        def name = nameMock

        when:
        planetService.findById(name)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected                  | nameMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by name"  | null

    }

    //endregion


    //region Private Methods Tests

    //region Mapping Tests

    @Unroll
    def "convertPlanetToPlanetResponse(Planet planet) - Success"() {

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
    def "convertPlanetRequestToPlanet(PlanetRequest request) - Success"() {

        given:
        def planetRequest = PlanetRequest.builder()
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
        def planetTest = planetService.convertPlanetRequestToPlanet(planetRequest)

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

    @Unroll
    def "checkPlanetExistence(PlanetRequest request) - Success"() {
        when:
        def planetRequest = Mock(PlanetRequest)

        then:
        planetService.checkPlanetExistence(planetRequest)
    }

    @Unroll
    def "checkPlanetExistence(PlanetRequest request) - Error"() {
        given:
        def planetRequest = planetRequestMock

        when:
        planetService.checkPlanetExistence(planetRequest)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected        | planetRequestMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error saving planets" | null

    }

    @Unroll
    def "checkPlanetNameAndId(String name, String id) - Success"() {
        when:
        def name = _ as String
        def id = _ as String

        then:
        planetService.checkPlanetNameAndId(name, id)
    }

    @Unroll
    def "checkPlanetNameAndId(String name, String id) - Error"() {
        given:
        def name = nameMock
        def id = idMock

        when:
        planetService.checkPlanetNameAndId(name, id)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected          | nameMock      | idMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error updating planets" | null          | null

    }

    @Unroll
    def "checkPlanetId(String id) - Success"() {
        when:
        def id = _ as String

        then:
        planetService.checkPlanetId(id)
    }

    @Unroll
    def "checkPlanetId(String id) - Error"() {
        given:
        def id = idMock

        when:
        planetService.checkPlanetId( id)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected               | idMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by id" | null

    }

    @Unroll
    def "checkPlanetName(Planet planet, boolean isGet) Success"() {
        when:
        def name = nameMock
        def isGet = isGetMock
        then:
        planetService.checkPlanetName(name, isGet)
        where:
        nameMock     | isGetMock
        _ as String  | true
        null         | false
    }

    @Unroll
    def "checkPlanetName(Planet planet, boolean isGet) Error "() {
        given:
        def name = nameMock
        def isGet = isGetMock
        when:
        planetService.checkPlanetName(name, isGet)
        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected                     | messageExpected                  | nameMock     | isGetMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by name"  | _ as String  | false
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by name"  | null         | true
    }

    @Unroll
    def "checkGetValidation(PlanetResponse response) - Success"() {
        given:
        def planetResponse = planetResponseMock

        when:
        def swapiResponse = planetService.checkGetValidation(planetResponse)

        then:
        swapiResponse == swapiResponseExpected

        where:
        swapiResponseExpected       | planetResponseMock
        Mock(SwapiResponse)         | Mock(PlanetResponse)
    }

    @Unroll
    def "checkGetValidation(PlanetResponse response) - Error"() {
        given:
        def planetResponse = planetResponseMock

        when:
        planetService.checkGetValidation(planetResponse)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected         | planetResponseMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets" | null

    }

    //endregion

    //endregion
}
