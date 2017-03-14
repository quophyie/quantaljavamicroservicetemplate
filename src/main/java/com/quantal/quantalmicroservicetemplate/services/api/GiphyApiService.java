package com.quantal.quantalmicroservicetemplate.services.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.concurrent.CompletableFuture;

/**
 * Created by dman on 12/03/2017.
 */


public interface GiphyApiService {


    // http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC
    /*public GiphyApiService(String baseUrl) {
        super(baseUrl);
    }*/

    @GET("http://api.giphy.com/v1/gifs/search")
    CompletableFuture<String> getGiphy(@Query("q") String query, @Query("api_key") String apiKey);
}
