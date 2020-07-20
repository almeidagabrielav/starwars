package com.empresa.starwars.repository

import com.empresa.starwars.domain.Planet
import spock.lang.Specification
import spock.lang.Unroll

class PlanetCacheTest extends Specification {

    //region Properties
    PlanetRepository planetRepository = Mock()
    def planetCache = new PlanetCache(planetRepository)
    //endregion

    //region Setup
    Planet planetTestMock

    def setup(){

        planetTestMock = Planet.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .build()

    }
    //endregion

    //region Public Methods Test

    @Unroll
    def "findById(String id)"(){
        given:
        def id = "id"

        def planetExpected = Planet.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .build()

        def testOptional = Optional.of(planetTestMock)


        when:
        planetRepository.findById(_ as String) >> testOptional
        def planet = planetCache.findById(id)

        then:
        planet == planetExpected
    }

    @Unroll
    def "findByName(String name)"(){
        given:
        def name = "name"

        def planetExpected = Planet.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .build()


        def planetTest = Planet.builder()
                .id("id")
                .name("name")
                .climate("climate")
                .terrain("terrain")
                .build();

        when:
        planetRepository.findByName(name) >> planetTest
        def planet = planetCache.findByName(name)

        then:
        planet == planetExpected

    }

    //endregion

}
