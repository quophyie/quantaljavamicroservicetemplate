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

## resources/db/migration
 This is a **`required directory`**.  This contains SQL for both migrations and seed files
