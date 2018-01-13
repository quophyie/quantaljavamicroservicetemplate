package com.quantal.quantalmicroservicetemplate.controllers;

import com.quantal.javashared.dto.CommonLogFields;
import com.quantal.javashared.logger.QuantalLoggerFactory;
import com.quantal.javashared.services.interfaces.MessageService;
import com.quantal.quantalmicroservicetemplate.controlleradvice.ExceptionHandlerCotrollerAdvice;
import com.quantal.quantalmicroservicetemplate.facades.MicroserviceFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.CompletableFuture;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartMatches;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dman on 24/03/2017.
 */

@RunWith(SpringRunner.class)
//@RunWith(MockitoJUnitRunner.class)
//@WebMvcTest(MicroserviceController.class)
//@DataJpaTest
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MicroserviceApplication.class)
public class MicroserviceControllerTests {

   // @Autowired
    private MockMvc mvc;


    private MicroserviceController microserviceController;

    @MockBean
    private MicroserviceFacade microserviceFacade;


    @MockBean
    private MessageService messageService;
    @Before
    public void setUp() {

        microserviceController = new MicroserviceController(microserviceFacade);

        ReflectionTestUtils.setField(microserviceController, "logger", QuantalLoggerFactory.getLogger(MicroserviceController.class, new CommonLogFields()));
        ExceptionHandlerCotrollerAdvice exceptionHandlerCotrollerAdvice = new ExceptionHandlerCotrollerAdvice(messageService);
        this.mvc = MockMvcBuilders.standaloneSetup(microserviceController)
                .setControllerAdvice(exceptionHandlerCotrollerAdvice)
                .build();
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
