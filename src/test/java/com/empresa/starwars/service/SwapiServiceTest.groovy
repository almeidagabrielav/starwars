package com.empresa.starwars.service

import com.empresa.starwars.clients.StarWarsClient
import com.empresa.starwars.domain.PlanetSwapiResponse
import com.empresa.starwars.domain.SwapiResponse
import com.empresa.starwars.repository.SwapiCache
import spock.lang.Specification
import spock.lang.Unroll

class SwapiServiceTest extends Specification {

    //region Properties
    StarWarsClient starWarsClient = Mock()
    SwapiCache swapiCache = Mock()
    def swapiService = new SwapiService(swapiCache)
    //endregion

    //region Setup
    PlanetSwapiResponse planetSwapiResponseMock;

    def setup(){

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

    //region Public Methods Test
    @Unroll
    def "getPlanets(String planet)"(){
        given:
        def planet = "planet"
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


        when:
        starWarsClient.getPlanets(planet) >> swapiResponseMock
        swapiCache.getPlanets(planet) >> swapiResponseMock
        def swapiResponse = swapiService.getPlanets(planet)

        then:
        swapiResponse == swapiResponseExpected

    }
    //endregion
}
