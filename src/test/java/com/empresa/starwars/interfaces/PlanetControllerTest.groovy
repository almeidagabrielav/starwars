package com.empresa.starwars.interfaces

import com.empresa.starwars.domain.PlanetRequest
import com.empresa.starwars.domain.PlanetResponse
import com.empresa.starwars.interfaces.PlanetController
import com.empresa.starwars.service.PlanetService
import com.empresa.starwars.util.IntegrationTestUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup


class PlanetControllerTest extends Specification {

    //region Properties
    PlanetService planetService = Mock()
    def planetController = new PlanetController(planetService)
    def mockMvc = standaloneSetup(planetController).build()
    //endregion

    //region Public Methods Tests
    @Unroll
    def "GET - [/planets]"(){
        given:
        planetService.findAll() >> findAllMock

        when:
        def response = mockMvc.perform(get("/planets")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response

        then:
        response.status == status.value()

        where:
        status                           | findAllMock
        HttpStatus.NOT_FOUND             | null
        HttpStatus.OK                    | new ArrayList<PlanetResponse>(Arrays.asList(Mock(PlanetResponse)))

    }

    @Unroll
    def "GET - [/planets/id]"(){
        given:
        planetService.findById(_ as String) >> findByIdMock

        when:
        def response = mockMvc.perform(get("/planets/" + _ as String)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response

        then:
        response.status == status.value()

        where:
        status                           | findByIdMock
        HttpStatus.NOT_FOUND             | null
        HttpStatus.OK                    | Mock(PlanetResponse)

    }

    @Unroll
    def "GET - [/planets/name]"(){
        given:
        planetService.findByName( _ as String) >> findByNameMock

        when:
        def response = mockMvc.perform(get("/planets?name=" + _ as String)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response

        then:
        response.status == status.value()

        where:
        status                           | findByNameMock
        HttpStatus.NOT_FOUND             | null
        HttpStatus.OK                    | Mock(PlanetResponse)

    }

    @Unroll
    def "POST - [/planets]"(){
        given:
        planetService.savePlanet(Mock(PlanetRequest)) >> savePlanet

        def planetRequest = PlanetRequest.builder()
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        planetService.savePlanet(_ as PlanetRequest) >> savePlanet

        when:
        def response = mockMvc.perform(post("/planets")
                .content(IntegrationTestUtil.convertToJson(planetRequest))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response


        then:
        response.status == status.value()

        where:
        status                 | name   | climate   | terrain   | savePlanet
        HttpStatus.BAD_REQUEST | null   | null      | null      | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | ""     | ""        | ""        | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | null   | "climate" | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | null      | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | "climate" | null      | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | ""     | "climate" | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | ""        | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | "climate" | ""        | Mock(PlanetResponse)
        HttpStatus.CREATED     | "name" | "climate" | "terrain" | Mock(PlanetResponse)
    }

    @Unroll
    def "PUT - [/planets/id]"(){
        given:
        planetService.updatePlanet(_ as String, Mock(PlanetRequest)) >> updatePlanet

        def planetRequest = PlanetRequest.builder()
                .name(name)
                .climate(climate)
                .terrain(terrain)
                .build()

        planetService.updatePlanet(_ as String, _ as PlanetRequest) >> updatePlanet

        when:
        def response = mockMvc.perform(put("/planets/" + _ as String)
                .content(IntegrationTestUtil.convertToJson(planetRequest))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response


        then:
        response.status == status.value()

        where:
        status                 | name   | climate   | terrain   | updatePlanet
        HttpStatus.BAD_REQUEST | null   | null      | null      | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | ""     | ""        | ""        | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | null   | "climate" | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | null      | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | "climate" | null      | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | ""     | "climate" | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | ""        | "terrain" | Mock(PlanetResponse)
        HttpStatus.BAD_REQUEST | "name" | "climate" | ""        | Mock(PlanetResponse)
        HttpStatus.OK          | "name" | "climate" | "terrain" | Mock(PlanetResponse)
    }

    @Unroll
    def "DELETE - [/planets/id]"(){
        given:
        planetService.deletePlanet(_ as String) >> deletePlanet
        when:
        def response = mockMvc.perform(delete("/planets/" + _ as String)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response

        then:
        response.status == status.value()

        where:
        status              | deletePlanet
        HttpStatus.OK       | null
    }
    //endregion

    //region Exceptions Test
//    @Unroll
//    def "GET - [/planets] - Exception"(){
//        when:
//            def response = mockMvc.perform(get("/planets")
//                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
//            ).andReturn().response
//
//        then:
//            response.status == HttpStatus.INTERNAL_SERVER_ERROR
//            thrown Exception
//    }
    //endregion
}
