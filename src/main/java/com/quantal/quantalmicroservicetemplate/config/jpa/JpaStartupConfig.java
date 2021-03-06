package com.quantal.quantalmicroservicetemplate.config.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
//@EnableJpaRepositories("com.quantal.quantalmicroservicetemplate.models")
@EnableJpaRepositories(basePackages = "com.quantal.quantalmicroservicetemplate.repositories")
@EntityScan("com.quantal.quantalmicroservicetemplate.models")
@EnableTransactionManagement
public class JpaStartupConfig {


  private Environment env;


  private final String DB_DRIVER = "org.postgresql.Driver";
  private final String DB_URL;
  private final String DB_USERNAME;
  private final String DB_PASSWORD;

  @Autowired
  public JpaStartupConfig(Environment env){
    this.env = env;
    DB_URL = env.getProperty("spring.datasource.url");
    DB_USERNAME = env.getProperty("spring.datasource.username");
    DB_PASSWORD = env.getProperty("spring.datasource.password");
  }
  @Bean
  public DataSource dataSource() {


    DriverManagerDataSource driver = new DriverManagerDataSource();
    driver.setDriverClassName(DB_DRIVER);
    driver.setUrl(DB_URL);
    driver.setUsername(DB_USERNAME);
    driver.setPassword(DB_PASSWORD);
    return driver;
  }

  @Bean
  public EntityManagerFactory entityManagerFactory() {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.quantal.quantalmicroservicetemplate.models");
    factory.setDataSource(dataSource());
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {

    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory());
    return txManager;
  }

}