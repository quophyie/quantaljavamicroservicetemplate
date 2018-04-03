package com.quantal.quantalmicroservicetemplate.config.shared;

import com.quantal.javashared.beanpostprocessors.LoggerInjectorBeanPostProcessor;
import com.quantal.javashared.dto.CommonLogFields;
import com.quantal.javashared.dto.LoggerConfig;
import com.quantal.javashared.dto.LogzioConfig;
import com.quantal.javashared.filters.EventAndTraceIdMdcPopulatingFilter;
import com.quantal.javashared.logger.QuantalLoggerFactory;
import com.quantal.javashared.objectmapper.NullSkippingOrikaBeanMapper;
import com.quantal.javashared.objectmapper.OrikaBeanMapper;
import com.quantal.javashared.services.implementations.MessageServiceImpl;
import com.quantal.javashared.services.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by dman on 12/04/2017.
 */

@Configuration
public class SharedConfig {

    @Value("${spring.version}")
    private String springVersion;

    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${spring.application.version}")
    private String moduleVersion;

    @Bean
    public NullSkippingOrikaBeanMapper nullSkippingOrikaBeanMapper() {
        return new NullSkippingOrikaBeanMapper();
    }

    @Bean
    public OrikaBeanMapper orikaBeanMapper() {
        return new OrikaBeanMapper();
    }


    @Bean
    public MessageService messageService (MessageSource messageSource){
        return new MessageServiceImpl(messageSource);
    }


    @Bean
    public CommonLogFields commonLogFields() throws UnknownHostException {
        String hostname = InetAddress.getLocalHost().getHostName();
        return new CommonLogFields(
                "java",
                "spring-boot",
                springVersion,
                moduleName,
                hostname,
                moduleVersion,
                Locale.UK.toString(),
                Instant.now().toString()
        );
    }

    @Bean
    public LogzioConfig logzioConfig(@Value("${logzio.token}") String logzioToken) {
        return QuantalLoggerFactory.createDefaultLogzioConfig(logzioToken, Optional.empty(), Optional.empty());
    }

    @Bean
    public LoggerConfig loggerConfig(LogzioConfig logzioConfig, CommonLogFields commonLogFields){
        LoggerConfig loggerConfig = LoggerConfig.builder().build();
        loggerConfig.setLogzioConfig(logzioConfig);
        loggerConfig.setCommonLogFields(commonLogFields);
        return loggerConfig;
    }

    @Bean
    public LoggerInjectorBeanPostProcessor loggerInjectorBeanPostProcessor(CommonLogFields commonLogFields, LogzioConfig logzioConfig){
        return new LoggerInjectorBeanPostProcessor(commonLogFields, logzioConfig);
    }

    @Bean
    @Order(1)
    public EventAndTraceIdMdcPopulatingFilter eventAndTraceIdPopulatingFilter(){
        return new EventAndTraceIdMdcPopulatingFilter();
    }

}
