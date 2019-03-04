package com.quantal.quantalmicroservicetemplate.config.api;

import com.quantal.javashared.aspects.RetrofitRequiredHeadersEnforcerAspectJAspect;
import com.quantal.javashared.constants.CommonConstants;
import com.quantal.javashared.constants.Events;
import com.quantal.javashared.dto.LogEvent;
import com.quantal.javashared.dto.LoggerConfig;
import com.quantal.javashared.logger.QuantalLogger;
import com.quantal.javashared.logger.QuantalLoggerFactory;
import com.quantal.javashared.util.CommonUtils;
import com.quantal.quantalmicroservicetemplate.services.api.GiphyApiService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.aspectj.lang.Aspects;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.quantal.javashared.constants.CommonConstants.EVENT_HEADER_KEY;
import static com.quantal.javashared.constants.CommonConstants.EVENT_KEY;
import static com.quantal.javashared.constants.CommonConstants.TRACE_ID_HEADER_KEY;
import static com.quantal.javashared.constants.CommonConstants.TRACE_ID_MDC_KEY;

/**
 * Created by dman on 14/03/2017.
 *
 * This class should contain the Retrofit API interfaces
 */

@Configuration
public class ApiConfig
{
    private final Environment env;
    private QuantalLogger logger;
    @Autowired
    @Value("${app.service-endpoints-not-requiring-mandatory-propagated-headers-uri-patterns}")
    private List<String> serviceEndpointsNotRequiringMandatoryPropagatedHeadersPatterns;

    @Autowired
    public ApiConfig(Environment env, LoggerConfig loggerConfig){
        this.env = env;
        logger = QuantalLoggerFactory.getLogzioLogger(ApiConfig.class, loggerConfig);
    }

    private String getRequestBody(Request request, Buffer buffer ){
        if (request.body() != null) {
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.readUtf8();
        }
        return  null;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.interceptors().add(chain -> {

            String requestBody = null;
            Request request = chain.request();
            Request newRequest;


            MDC.put(EVENT_KEY, request.header(EVENT_HEADER_KEY));
            MDC.put(TRACE_ID_MDC_KEY, request.header(TRACE_ID_HEADER_KEY));
            LogEvent event = new LogEvent(request.header(CommonConstants.EVENT_HEADER_KEY));

            boolean bEndpointRequiresMandatoryPropagatedHeaders = CommonUtils
                    .isMandatoryPropagatedHeadersRequiredToCallEndpoint(request.url().toString(),
                            serviceEndpointsNotRequiringMandatoryPropagatedHeadersPatterns);

            final boolean bAllRequiredHeadersFound = request
                    .headers()
                    .names()
                    .stream()
                    .filter(name -> name.equalsIgnoreCase(EVENT_HEADER_KEY) || name.equalsIgnoreCase(TRACE_ID_HEADER_KEY))
                    .count() == 2;

            if(!bAllRequiredHeadersFound && bEndpointRequiresMandatoryPropagatedHeaders){
                String msg = String.format("The required request headers %s and %s were not found. " +
                                "Please make sure that your api interface method " +
                                "has headers %s and %s ",
                        EVENT_HEADER_KEY, TRACE_ID_HEADER_KEY, EVENT_HEADER_KEY, TRACE_ID_HEADER_KEY);
                RuntimeException exception =  new IllegalStateException(msg);
                throw exception;

            }

            final Buffer buffer = new Buffer();

            long t1 = System.nanoTime();

            newRequest = request.newBuilder()
                                .build();

            logger.info(String.format("Sending request for %s",newRequest.url()),
                    new HashMap<String, Object>() {{
                        put("requestUrl",newRequest.url().url());
                        put("requestUrlParts",newRequest.url());
                        put("connection",chain.connection());
                        put("headers", newRequest.headers());
                        put("requestBody", getRequestBody(request, buffer));
                    }},
                    newRequest.url(), chain.connection(), newRequest.headers(), requestBody,
                    event
            );

            Response response = chain.proceed(newRequest);

            long t2 = System.nanoTime();
            String responseBody =response.body().string();

            if (StringUtils.isEmpty(MDC.get(EVENT_KEY))
                    && !StringUtils.isEmpty(request.header(EVENT_HEADER_KEY))){
                MDC.put(EVENT_KEY, request.header(EVENT_HEADER_KEY));

            }

            if (StringUtils.isEmpty(MDC.get(TRACE_ID_MDC_KEY))
                    && !StringUtils.isEmpty(request.header(TRACE_ID_HEADER_KEY))){
                MDC.put(TRACE_ID_MDC_KEY, request.header(TRACE_ID_HEADER_KEY));

            }

            if (event.getEvent().equalsIgnoreCase(Events.DEFAULT_REQUEST_EVENT)) {
                event = new LogEvent(Events.DEFAULT_RESPONSE_EVENT);
            }

            logger.info(String.format("Received response for %s",
                    response.request().url()),
                    new HashMap<String, Object>() {{
                        put("duration", (t2 - t1) / 1e6d);
                        put("headers", response.headers());
                        put("responseBody",responseBody);
                        put("statusCode",response.code());
                    }}, event);

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
