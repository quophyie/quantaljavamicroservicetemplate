package com.quantal.quantalmicroservicetemplate.config.api;

import com.quantal.javashared.aspects.RetrofitRequiredHeadersEnforcerAspectJAspect;
import com.quantal.quantalmicroservicetemplate.services.api.GiphyApiService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.aspectj.lang.Aspects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dman on 14/03/2017.
 *
 * This class should contain the Retrofit API interfaces
 */

@Configuration
public class ApiConfig
{

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();;
        Logger logger = LoggerFactory.getLogger("LoggingInterceptor");

        builder.interceptors().add(chain -> {
            Request request = chain.request();
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String requestBody = buffer.readUtf8();
            long t1 = System.nanoTime();
            logger.info(String.format("Sending request %s on %s%n%s %s",
                    request.url(), chain.connection(), request.headers(), requestBody));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            String responseBody =response.body().string();
            logger.info(String.format("Received response for %s in %.1fms%n%s %s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(), responseBody));

            //return response;
            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseBody))
                    .build();
        });

        OkHttpClient client = builder.build();
        return client;
    }

    @Bean
    public RetrofitRequiredHeadersEnforcerAspectJAspect requestHeadersAspect(){
        RetrofitRequiredHeadersEnforcerAspectJAspect requestHeadersAspect = Aspects.aspectOf(RetrofitRequiredHeadersEnforcerAspectJAspect.class);
        return  requestHeadersAspect;
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://testapi")
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(new StringConverterFactory())
                //.addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Bean
    public GiphyApiService giphyApiService(Retrofit retrofit) {
        GiphyApiService service = retrofit.create(GiphyApiService.class);
        return service;
    }
}
