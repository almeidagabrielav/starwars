package com.empresa.starwars.service

import com.empresa.starwars.configuration.exceptions.GenericApiException
import com.empresa.starwars.domain.Planet
import com.empresa.starwars.domain.PlanetRequest
import com.empresa.starwars.domain.PlanetResponse
import com.empresa.starwars.domain.PlanetSwapiResponse
import com.empresa.starwars.domain.SwapiResponse
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ValidationTest extends Specification {
    //region Property
    def validation = new Validation()
    @Shared
    List<PlanetSwapiResponse> listPlanetSwapiResponse = new ArrayList<>()
    //endregion

    //region Public Methods Test

    @Unroll
    def "checkSwapiPlanetExistence(SwapiResponse response, PlanetRequest request) Success"() {
        when:

        List<PlanetSwapiResponse> results = new ArrayList<>()

        def planetSwapiResponse = PlanetSwapiResponse.builder()
                .name("name 1")
                .terrain("terrain 1")
                .climate("climate 1")
                .films(new ArrayList<String>(Arrays.asList(_ as String)) )

        results << planetSwapiResponse

        def swapiResponse = SwapiResponse.builder()
                .results(results)
                .count(1)
                .build()

        def planetRequest = PlanetRequest.builder()
                .name("name 1")
                .terrain("terrain 1")
                .climate("climate 1")
                .build()

        then:
        validation.checkSwapiPlanetExistence(swapiResponse, planetRequest)

    }

    @Unroll
    def "checkSwapiPlanetExistence(SwapiResponse response, PlanetRequest request) Error "() {
        given:
        def swapiResponse = swapiResponseMock
        def planetRequest = planetRequestMock

        def planetSwapiResponse = PlanetSwapiResponse.builder()
                .name("name 1")
                .terrain("terrain 1")
                .climate("climate 1")
                .films(new ArrayList<String>(Arrays.asList(_ as String)) )

        listPlanetSwapiResponse << planetSwapiResponse

        when:
        validation.checkSwapiPlanetExistence(swapiResponse, planetRequest)

        then:
        GenericApiException ex = thrown()
        ex.message == messageExpected
        ex.statusCode == codeExpected

        where:
        codeExpected           | messageExpected                                                                                                | swapiResponseMock                                                         | planetRequestMock
        HttpStatus.BAD_REQUEST | "It is not possible to register a planet with that name because it is not in the public api of Star Wars."     | null                                                                      | Mock(PlanetRequest)
        HttpStatus.BAD_REQUEST | "It is not possible to register a planet with that name because it is not in the public api of Star Wars."     | SwapiResponse.builder().results(listPlanetSwapiResponse).count(1).build() | PlanetRequest.builder().name("nam").terrain("terrain 1").climate("climate 1").build()
    }

    @Unroll
    def "checkPlanetId(Optional<Planet> planet) Success"() {
        when:
        def planet = planetMock
        then:
        validation.checkPlanetId(planet)
        where:
        planetMock   | _
        Mock(Planet) | _
        null         | _
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
        validation.checkPlanetNameAndId(planet, id)
        where:
        planetMock                             | idMock
        Planet.builder().id("id 1").build()    | "id 1"
        Planet.builder().id("id 2").build()    | "id 2"
        Planet.builder().id("id 3").build()    | "id 3"
    }

    @Unroll
    def "checkGetValidation(SwapiResponse response) Error "() {
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
    //endregion

    //region Private Methods Test

    //endregion
}
