package com.quantal.quantalmicroservicetemplate.config.api;

import com.quantal.quantalmicroservicetemplate.services.api.GiphyApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

/**
 * Created by dman on 14/03/2017.
 *
 * This class should contain the Retrofit API interfaces
 */

@Configuration
public class ApiConfig
{

    @Bean
    public GiphyApiService giphyApiService(Retrofit retrofit) {
        GiphyApiService service = retrofit.create(GiphyApiService.class);
        return service;
    }
}
