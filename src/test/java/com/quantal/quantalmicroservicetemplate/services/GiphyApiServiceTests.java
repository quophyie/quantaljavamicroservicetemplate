package com.quantal.quantalmicroservicetemplate.services;

import com.quantal.quantalmicroservicetemplate.services.api.GiphyApiService;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.ExecutionException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartMatches;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by dman on 22/03/2017.
 */


//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class GiphyApiServiceTests {

    @ClassRule
     public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode("simulation.json");

    private Retrofit retrofit;
    private GiphyApiService giphyApiService;

    @Before
    public void setUp() {
        retrofit =  new Retrofit.Builder()
                .baseUrl("https://testapi")
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(new StringConverterFactory())
                //.addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                //.client(client)
                .build();
        giphyApiService = retrofit.create(GiphyApiService.class);
    }

    @Test
    public void shouldBeAbleToGetPokenHoverfly() throws ExecutionException, InterruptedException {

        // When
        String result = giphyApiService.getPokemon(20, 0,"EVENT", "TRACE_ID").get();
        String jsonStringResult = StringEscapeUtils.unescapeJson(result);

        //Then
        assertThatJson(jsonStringResult)
                .node("count").isEqualTo(251)
                .node("results")
                .matches(hasItem(jsonPartMatches("name", equalTo("drizzle"))))
                .isArray();
    }
}
