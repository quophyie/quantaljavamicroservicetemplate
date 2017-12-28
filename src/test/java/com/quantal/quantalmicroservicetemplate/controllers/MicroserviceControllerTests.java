package com.quantal.quantalmicroservicetemplate.controllers;

import com.quantal.quantalmicroservicetemplate.MicroserviceApplication;
import com.quantal.quantalmicroservicetemplate.facades.MicroserviceFacade;
import com.quantal.javashared.dto.CommonLogFields;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartMatches;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;

/**
 * Created by dman on 24/03/2017.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(MicroserviceController.class)
//@DataJpaTest
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MicroserviceApplication.class)
public class MicroserviceControllerTests {

    @Autowired
    private MockMvc mvc;

    private MicroserviceController microserviceController;

    @MockBean
    private MicroserviceFacade microserviceFacade;


    @Before
    public void setUp() {

        microserviceController = new MicroserviceController(microserviceFacade, new CommonLogFields(),null);
    }

    @Test
    public void shouldGetGiphyGivenQuery() throws Exception {

        String jsonResult = "{\"data\":[{\"type\":\"gif\",\"id\":\"jTnGaiuxvvDNK\",\"slug\":\"funny-cat-jTnGaiuxvvDNK\",\"url\":\"http:\\/\\/giphy.com\\/gifs\\/funny-cat-jTnGaiuxvvDNK\",\"bitly_gif_url\":\"http:\\/\\/gph.is\\/2cKVPVQ\"}]}";
        given(this.microserviceFacade.getFunnyCat())
                .willReturn(CompletableFuture.completedFuture(jsonResult));

        MvcResult asyscResult = this.mvc.perform(get("/users/").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        mvc.perform(asyncDispatch(asyscResult))
            .andExpect(status().isOk())
            .andExpect(
                json()
                    .node("data")
                    .isArray())
            .andExpect(
                json()
                    .node("data")
                    .matches(hasItem(jsonPartMatches("type", equalTo("gif")))));


    }
}
