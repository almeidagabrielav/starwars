package com.empresa.starwars.service

import com.empresa.starwars.configuration.exceptions.GenericApiException
import com.empresa.starwars.domain.Planet
import com.empresa.starwars.domain.PlanetRequest
import com.empresa.starwars.domain.PlanetSwapiResponse
import com.empresa.starwars.domain.SwapiResponse
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ValidationTest extends Specification {

    //region Property
    def validation = new Validation()
    //endregion

    //region Setup
    Planet planetTestMock
    ArrayList<Planet> planetsTestsMock
    PlanetSwapiResponse planetSwapiResponseMock
    @Shared
    SwapiResponse swapiResponseMock

    def setup(){

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

        def resultsMock = new ArrayList()
        resultsMock.add(planetSwapiResponseMock)

        swapiResponseMock = SwapiResponse.builder()
                .count(1)
                .results(resultsMock)
                .build()
    }
    //endregion

    //region Public Methods Test

    @Unroll
    def "checkSwapiPlanetExistence(SwapiResponse response, PlanetRequest request) Success"() {
        when:

        def planetRequest = PlanetRequest.builder()
                .name("name")
                .terrain("terrain")
                .climate("climate")
                .build()

        then:
        validation.checkSwapiPlanetExistence(swapiResponseMock, planetRequest)

    }

    @Unroll
    def "checkSwapiPlanetExistence(SwapiResponse response, PlanetRequest request) Error "() {
        given:
        def swapiResponse = swapiResponseMock
        def planetRequest = planetRequestMock

        when:
        validation.checkSwapiPlanetExistence(swapiResponse, planetRequest)

        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected           | messageExpected                                                                                                | swapiResponseMock | planetRequestMock
        HttpStatus.BAD_REQUEST | "It is not possible to register a planet with that name because it is not in the public api of Star Wars."     | null              | Mock(PlanetRequest)
        HttpStatus.BAD_REQUEST | "It is not possible to register a planet with that name because it is not in the public api of Star Wars."     | swapiResponseMock | Mock(PlanetRequest)
    }

    @Unroll
    def "checkPlanetId(Planet planet) Success"() {
        when:
        def planet = planetMock
        then:
        validation.checkPlanetId(planet)
        where:
        planetMock   | _
        Mock(Planet) | _
    }

    @Unroll
    def "checkPlanetId(Optional<Planet> planet) Error "() {
        given:
        def planet = planetMock
        def isGet = isGetMock
        when:
        validation.checkPlanetName(planet, isGet)
        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected           | messageExpected                                            | planetMock   | isGetMock
        HttpStatus.BAD_REQUEST | "There is already a registered planet with the same name." | Mock(Planet) | false
        HttpStatus.BAD_REQUEST | "There is no planet with that name."                       | null         | true
    }

    @Unroll
    def "checkPlanetName(Planet planet, boolean isGet) Success"() {
        when:
        def planet = planetMock
        def isGet = isGetMock
        then:
        validation.checkPlanetName(planet, isGet)
        where:
        planetMock   | isGetMock
        Mock(Planet) | true
        null         | false
    }

    @Unroll
    def "checkPlanetName(Planet planet, boolean isGet) Error "() {
        given:
        def planet = planetMock
        def isGet = isGetMock
        when:
        validation.checkPlanetName(planet, isGet)
        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected           | messageExpected                                            | planetMock   | isGetMock
        HttpStatus.BAD_REQUEST | "There is already a registered planet with the same name." | Mock(Planet) | false
        HttpStatus.BAD_REQUEST | "There is no planet with that name."                       | null         | true
    }

    @Unroll
    def "checkPlanetNameAndId(Planet planet, String id) Success"() {
        when:
        def planet = planetMock
        def id = idMock
        then:
        validation.checkPlanetNameAndId(planet, id)
        where:
        planetMock                             | idMock
        Planet.builder().id("id 1").build()    | "id 1"
        Planet.builder().id("id 2").build()    | "id 2"
        Planet.builder().id("id 3").build()    | "id 3"
    }

    @Unroll
    def "checkPlanetNameAndId(Planet planet, String id) Error "() {
        given:
        def planet = planetMock
        def id = idMock
        when:
        validation.checkPlanetNameAndId(planet, id)
        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected           | messageExpected                                                                | planetMock                             | idMock
        HttpStatus.BAD_REQUEST | "There is already a registered planet with the same name with a different id." | Planet.builder().id("id 1").build()    | "id 2"
        HttpStatus.BAD_REQUEST | "There is already a registered planet with the same name with a different id." | Planet.builder().id("id 2").build()    | "id 3"
        HttpStatus.BAD_REQUEST | "There is already a registered planet with the same name with a different id." | Planet.builder().id("id 3").build()    | "id 4"
    }

    @Unroll
    def "checkGetValidation(SwapiResponse response) Success"() {
        when:
        def swapiResponse = swapiResponseMock
        then:
        validation.checkGetValidation(swapiResponse)
    }

    @Unroll
    def "checkGetValidation(SwapiResponse response) Error "() {
        given:
        def swapiResponse = swapiResponseMockTest
        when:
        validation.checkGetValidation(swapiResponse)
        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected           | messageExpected                      | swapiResponseMockTest
        HttpStatus.BAD_REQUEST | "There is no planet with that name." | null
    }
    //endregion

    //region Private Methods Test

    @Unroll
    def "checkResponseSize(int size) Success"() {
        when:
        def size = 1

        then:
        validation.checkResponseSize(size)

    }

    @Unroll
    def "checkResponseSize(int size) Error "() {
        given:
        def size = sizeMock
        when:
        validation.checkResponseSize(size)
        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected
        where:
        codeExpected           | messageExpected                                                                                                                         | sizeMock
        HttpStatus.BAD_REQUEST | "There is no planet with the data reported."                                                                                            | 0
        HttpStatus.BAD_REQUEST | "It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search." | 2
        HttpStatus.BAD_REQUEST | "It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search." | 3
        HttpStatus.BAD_REQUEST | "It was not possible to obtain the number of appearances in films because the method returned more than one result in the name search." | 4
    }

    //endregion
}
