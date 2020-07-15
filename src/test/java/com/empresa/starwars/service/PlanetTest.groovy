package com.empresa.starwars.service

import com.empresa.starwars.domain.PlanetDTO
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
        HttpStatus.OK                    | new ArrayList<PlanetDTO>(Arrays.asList(Mock(PlanetDTO)))
        //HttpStatus.INTERNAL_SERVER_ERROR | Exception

    }

    @Unroll
    def "GET - [/planets] - Exception"(){
        when:
        def response = mockMvc.perform(get("/planets")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andReturn().response

        then:
        response.status == HttpStatus.INTERNAL_SERVER_ERROR
        thrown Exception
    }
}
