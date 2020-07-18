package com.empresa.starwars.service

import com.empresa.starwars.clients.StarWarsClient
import com.empresa.starwars.domain.SwapiResponse
import com.empresa.starwars.repository.SwapiCache
import spock.lang.Unroll

class SwapiServiceTest extends spock.lang.Specification {

    //region Properties
    StarWarsClient starWarsClient = Mock()
    def swapiCache = new SwapiCache(starWarsClient)
    def swapiService = new SwapiService(swapiCache)
    //endregion

    @Unroll
    def "getPlanets(String planet)"(){
        given:
        def planet = planetMock
        starWarsClient.getPlanets(planet) >> starWarsClientMock
        swapiCache.getPlanets(planet) >> swapiCacheMock

        when:
        def swapiResponse = swapiService.getPlanets(planet)

        then:
        swapiResponse == swapiResponseExpected

        where:
        planetMock     | swapiCacheMock       | starWarsClientMock    | swapiResponseExpected
        "planet 1"     | Mock(SwapiResponse)  | Mock(SwapiResponse)   | Mock(SwapiResponse)
        "planet 2"     | Mock(SwapiResponse)  | Mock(SwapiResponse)   | Mock(SwapiResponse)
        "planet 3"     | Mock(SwapiResponse)  | Mock(SwapiResponse)   | Mock(SwapiResponse)
    }  }
