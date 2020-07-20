package com.empresa.starwars.service

import com.empresa.starwars.configuration.exceptions.GenericApiException
import com.empresa.starwars.domain.Planet
import com.empresa.starwars.domain.PlanetRequest
import com.empresa.starwars.domain.PlanetResponse
import com.empresa.starwars.domain.PlanetSwapiResponse
import com.empresa.starwars.domain.SwapiResponse
import com.empresa.starwars.repository.PlanetCache
import com.empresa.starwars.repository.PlanetRepository
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

class PlanetServiceTest extends Specification {

    //region Properties
    PlanetRepository planetRepository = Mock()
    SwapiService swapiService = Mock()
    Validation validation = Mock()
    PlanetCache planetCache = Mock()
    def planetService = new PlanetService(planetRepository, swapiService, validation, planetCache)

    //endregion

    //region Setup
    Optional<Planet> optionalPlanetTestMock
    Planet planetTestMock
    ArrayList<Planet> planetsTestsMock;
    PlanetSwapiResponse planetSwapiResponseMock;

    def setup(){
        optionalPlanetTestMock = new Optional<Planet>();

        planetTestMock = Planet.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .build()
        planetsTestsMock = new ArrayList<>()
        planetsTestsMock.add(planetTestMock)

        def films = new ArrayList<String>()
        films.add("film")
        planetSwapiResponseMock = PlanetSwapiResponse.builder()
                .name("name")
                .terrain("terrain")
                .climate("climate")
                .films(films)
                .build()
    }
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
        planetRepository.findByName( _ as  String) >> Mock(Planet)
        validation.checkPlanetName(_ as Planet, _ as Boolean) >> null
        swapiService.getPlanets(_ as String) >> Mock(SwapiResponse)
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

        Planet planet = Planet.builder().build()

        when:
        planetRepository.findById(_ as String) >> Optional.of(Mock(Planet))
        validation.checkPlanetId(planet)
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

        Planet planet = Planet.builder().build()
        when:
        planetRepository.findById(_ as String) >> Optional.of(Mock(Planet))
        validation.checkPlanetId(planet)
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
        Planet planet = Planet.builder().build()

        when:
        planetRepository.findById(id) >> {throw  new Exception()}
        validation.checkPlanetId(planet)
        planetService.deletePlanet(id)

        then:
        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected                     | idMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by id"       | null

    }

    @Unroll
    def "findAll()- Success - ListEmpty"() {
        when:
        planetRepository.findAll() >> new ArrayList<Planet>()
        def planetResponse = planetService.findAll()

        then:
        Objects.nonNull(planetResponse)
    }

    @Unroll
    def "findAll() - Success"() {
        given:
        def resultsMock = new ArrayList()
        resultsMock.add(planetSwapiResponseMock)

        def swapiResponseMock = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()

        def planetResponseExpected = PlanetResponse.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .countFilmsAppearances(1)
                .build()

        def planetsExpected = new ArrayList<PlanetResponse>()
        planetsExpected.add(planetResponseExpected)

        when:
        planetRepository.findAll() >> planetsTestsMock
        swapiService.getPlanets(_ as String) >> swapiResponseMock
        validation.checkGetValidation(_ as SwapiResponse) >> null
        def planetsResponse = planetService.findAll()

        then:
        planetsResponse == planetsExpected
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
        def id  = "id"
        def resultsMock = new ArrayList()
        resultsMock.add(planetSwapiResponseMock)
        def swapiResponseMock = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()
        def planetResponseExpected = PlanetResponse.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .countFilmsAppearances(1)
                .build()

        def planet = Planet.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .build()

        def planetsExpected = new ArrayList<PlanetResponse>()
        planetsExpected.add(planetResponseExpected)

        when:
        planetRepository.findById(id) >> optionalPlanetTestMock
        validation.checkPlanetId(_ as Optional<Planet>) >> null
        swapiService.getPlanets(_ as String) >> swapiResponseMock
        validation.checkGetValidation(_ as SwapiResponse) >> null
        planetCache.findById(_ as String) >> planet
        def planetResponse = planetService.findById(id)

        then:
        planetResponse == planetResponseExpected

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
        def name = "name"
        def resultsMock = new ArrayList()
        resultsMock.add(planetSwapiResponseMock)
        def swapiResponseMock = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()
        def planetResponseExpected = PlanetResponse.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .countFilmsAppearances(1)
                .build()
        def planetsExpected = new ArrayList<PlanetResponse>()
        planetsExpected.add(planetResponseExpected)

        when:
        planetCache.findByName(name) >> planetTestMock
        planetRepository.findByName(name) >> planetTestMock
        swapiService.getPlanets(_ as String) >> swapiResponseMock
        validation.checkPlanetName(_ as Planet, _ as boolean) >> null
        validation.checkGetValidation(_ as SwapiResponse) >> null
        def planetResponse = planetService.findByName(name)

        then:
        planetResponse == planetResponseExpected

    }

    @Unroll
    def "findByName(String name)  - Error"() {
        when:
        planetService.findByName(_ as String)

        then:
        def ex = thrown(GenericApiException)

        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected                     | messageExpected
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by name"
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
        given:
        def planetRequest = Mock(PlanetRequest)
        def resultsMock = new ArrayList()
        resultsMock.add(planetSwapiResponseMock)

        def swapiResponseMock = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()

        when:
        swapiService.getPlanets(_ as String) >> swapiResponseMock
        validation.checkSwapiPlanetExistence(_ as SwapiResponse, _ as PlanetRequest) >> null

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
        given:
        def name = nameMock
        def id =idMock

        when:
        planetRepository.findByName(_ as String) >> Mock(Planet)
        validation.checkPlanetName(_ as Planet, _ as String) >> null

        then:
        planetService.checkPlanetNameAndId(name, id)

        where:
        nameMock | idMock
        "name 1" | "id 1"
        "name 2" | "id 2"
        "name 3" | "id 3"
    }

    @Unroll
    def "checkPlanetNameAndId(String name, String id) - Error"() {
        given:
        def name = nameMock
        def id = idMock

        when:
        validation.checkPlanetNameAndId(null, id) >> { throw  new Exception()}
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
        given:
        Planet planet = Planet.builder().build();
        def id = "teste"
        when:
        planetRepository.findById(id) >> Optional.of(planet)
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
        given:
        def name = nameMock
        def isGet = isGetMock

        when:
        planetRepository.findByName(_ as String) >> Mock(Planet)
        validation.checkPlanetName(_ as Planet, _ as boolean) >> null

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
        planetRepository.findAllById(name, isGet) >> null
        validation.checkPlanetName(name, isGet) >> Exception
        planetService.checkPlanetName(name, isGet)

        then:

        def ex = thrown(GenericApiException)
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected                     | messageExpected                  | nameMock     | isGetMock
        HttpStatus.INTERNAL_SERVER_ERROR | "Error getting planets by name"  | null         | true
    }

    @Unroll
    def "checkGetValidation(PlanetResponse response) - Success"() {
        given:
        def resultsMock = new ArrayList()
        resultsMock.add(planetSwapiResponseMock)

        def swapiResponseMock = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()

        def swapiResponseExpected = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()

        def planetResponseMock = PlanetResponse.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .countFilmsAppearances(1)
                .build()

        when:
        swapiService.getPlanets(_ as String) >> swapiResponseMock
        validation.checkGetValidation(_ as SwapiResponse) >> null
        def swapiResponse = planetService.checkGetValidation(planetResponseMock)

        then:
        swapiResponse == swapiResponseExpected

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
