# Quantal Java Microservice Template

This is a template / skeleton for a Java microservice.
To use the template, simply clone this repository and update the following

## base package name


In your POM, update the **`artifactId`** and **`name`** to values of your choice

Refactor the base package name (i.e. **`com.quantal.quantalmicroservicetemplate`**) to a name of your choice
 
## Environment Variables
 Somee default environment variables 
 - **`API_GATEWAY_ENDPOINT`** - The base url of the api gateway - default should be **`http://localhost:8001`**
 
 - **`LOGZIO_TOKEN`** - The [Logz.io](http://logz.io) token to use to send logs to [Logz.io](http://logz.io)

## application.properties  (located at src/main/java/resource)
  
  Change the following to the correct values
  
 - **`spring.datasource.url`**
 - **`spring.datasource.url`**
 - **`spring.datasource.username`**
 - **` spring.datasource.platform`**
 - **` spring.datasource.initialize`**
 - **`flyway.url`**
 - **`flyway.user`**
 
## JpaStartupConfig.java  (/src/main/java/com/quantal/quantalmicroservicetemplate/config/jpa/) 

 - Change the base package in the annotation **`@EnableJpaRepositories`** to your desired package
 - Change the base package in the annotation **`@EntityScan`** to your desired package
 - Change the base package in the method call to  **`factory.setPackagesToScan("com.quantal.quantalmicroservicetemplate.models")`** to your desired package
 
## SharedConfig.java (/src/main/java/com/quantal/quantalmicroservicetemplate/config/jpa/) 

 This contains bean configuratons that do not belong in either **`JpaStartupConfig.java`**
 **`ApiConfig.java`** and  **`WebStartupConfig.java`**


## Configuring the Quantal Logger
 The **`Quantal Logger`** wraps around the  [GoDaddy Logger](https://github.com/godaddy/godaddy-logger) and the [Logzio Java Sender](https://github.com/logzio/logzio-java-sender)
 and makes simple it for us to create structured logs that are sent to [Logz.io](http://logz.io). Among other things, the **`Quantal Logger`**  allows you to 
 define common log fields that should be included in all logs and also define fields that are required in all logs before the log is considered
 a valid log line (**`traceId`** and **`event`** by default).The **`LoggerConfig`** object is used to set configurations for the logger. For example 
 if you do not want the **`traceId`** and **`event`** as required log fields in every log, you can set  **`eventIsRequired`** and **`traceIdIsRequired`** to `false`. As another example if you wanted tp configure
 other custom required fields, you can add these new custom required fields to the **`requiredLogFields`** map on the **`LoggerConfig`** object.  The [GoDaddy Logger](https://github.com/godaddy/godaddy-logger) is itself an 
 implementation of **`org.slf4j.Logger`**. You can configure the **`Quantal Logger`** manually using the **`com.quantal.javashared.logger.QuantalLoggerFactory`** for  each class.
 However, you create the configuration that is used globally by the  **`Quantal Logger`** and then
 use  **`@InjectLogger`** annotation inject a class specific logger. The **`@InjectLogger`** annotation is is implemented as a bean post processor which
 needs to be configured. The bean post processor that is used to by the **`@InjectLogger`** annotation is 
 implemented in **`com.quantal.javashared.beanpostprocessors.LoggerInjectorBeanPostProcessor`.**. To configure the **`com.quantal.javashared.beanpostprocessors.LoggerInjectorBeanPostProcessor`** and make the 
 **`@InjectLogger`** annotation functional , copy and paste the following into **`@Configuration`** annotated classes
 
 ```java

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
```

To use **`@InjectLogger`** annotation, you will do something akin to the following 

```java

@org.springframework.stereotype.Component
public class MyClass{
     @com.quantal.javashared.annotations.logger.InjectLogger
      private QuantalLogger logger;
     
     public void myMethod(){
         logger.with("traceId", "SomeTraceId")
                .with("event", "SOME_EVENT")
                .debug("doing some task");
     }
}

```

  **Manual Configuration**
  
  ```java

CommonLogFields commonLogFields =  new CommonLogFields();
		commonLogFields.setFrameworkVersion(new String("1.0.1"));
		commonLogFields.setFramework(1.00);
		LoggerConfig loggerConfig = LoggerConfig.builder()

												.commonLogFields(commonLogFields)
				  								.logzioConfig(QuantalLoggerFactory.createDefaultLogzioConfig("MY_LOGZIO_TOKEN", Optional.of(true), Optional.empty()))
												.build();
		QuantalLogger logger = QuantalLoggerFactory.getLogzioLogger(MyClass.class, loggerConfig);
		Exception nullPointerEx = new NullPointerException("Some NullPointerException");
		logger.throwing(nullPointerEx, new LogEvent("EXCEPTION_EVENT"), new LogField(SUB_EVENT_KEY, String.format("SOME_EX_SUBEVENT %s", nullPointerEx.getMessage())), new LogTraceId("TEST_EX_TRACE_ID"));
		logger.error(new ObjectAppendingMarker("TestMarker", "testMarkerFieldName"), "test markerMsg", new NullPointerException(), new LogTraceId("TEST_TRACE_ID"), new LogEvent("TEST_EVENT"));
		logger.with(new LogTraceId("TEST_TRACE_ID")).with(new LogEvent("TEST_EVENT")).with("StringTest1").with("StringTest2").info("Some string message");
```

  ### Asynchronous Controllers Returning CompletableFutures
  
  Async controllers that return **`CompletableFutures`** should extend **`BaseControllerAsync`**. This **`BaseControllerAsync`**
  makes a available the methods **`applyJsonView`** and **`applyJsonViewAsync`**. These allow us to apply **`@JsonView`** annotations to controller methods 
  that return   **`CompletableFutures`** . For example
  
  ```java
  
    @GetMapping(value="/{userId}")
    public CompletableFuture<ResponseEntity> findUserbyId(@PathVariable Long userId){
      return userManagementFacade
              .findUserById(userId)
              .thenApply(responseEntity -> applyJsonView(responseEntity, UserViews.CreatedAndUpdatedUserView.class, objectMapper));
    }


## resources/db/migration
 This is a **`required directory`**.  This contains SQL for both migrations and seed files
