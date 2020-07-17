package com.empresa.starwars.service

import com.empresa.starwars.domain.PlanetResponse
import com.empresa.starwars.interfaces.PlanetController
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup


class PlanetTest extends Specification {

    PlanetService planetService = Mock()
    def planetController = new PlanetController(planetService)
    def mockMvc = standaloneSetup(planetController).build()

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

    def "GET - [/planets/id]"(){
        given:
            planetService.findById("5106106106") >> findByIdMock
        when:
            def response = mockMvc.perform(get("/planets/5106106106")
                   .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            ).andReturn().response

        then:
            response.status == status.value()

        where:
            status                           | findByIdMock
            HttpStatus.NOT_FOUND             | null
            HttpStatus.OK                    | Mock(PlanetResponse)

    }

    def "GET - [/planets/name]"(){
        given:
        planetService.findByName("name") >> findByNameMock
        when:
        def response = mockMvc.perform(get("/planets?name=name")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response

        then:
        response.status == status.value()

        where:
        status                           | findByNameMock
        HttpStatus.NOT_FOUND             | null
        HttpStatus.OK                    | Mock(PlanetResponse)

    }

    def "POST - [/planets]"(){
        when:
        def response = mockMvc.perform(post("/planets",
                {
                            name        : nameVal
                            climate     : climateVal
                            terrain     : terrainVal
                         })
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response


        then:
        response.status == status.value()

        where:
        status                              |    nameVal     |      climateVal      |       terrainVal
        HttpStatus.BAD_REQUEST              |     null       |          null        |           null
        HttpStatus.BAD_REQUEST              |     ""         |          ""          |           ""
        HttpStatus.BAD_REQUEST              |     null       |       "climate"      |         "terrain"
        HttpStatus.BAD_REQUEST              |    "name"      |          null        |         "terrain"
        HttpStatus.BAD_REQUEST              |    "name"      |       "climate"      |           null
        HttpStatus.CREATED                  |    "name"      |       "climate"      |         "terrain"
    }

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
}
