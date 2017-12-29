package com.quantal.quantalmicroservicetemplate.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.quantal.quantalmicroservicetemplate.facades.MicroserviceFacade;
import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.jsonviews.MicroserviceViews;
import com.quantal.javashared.controller.BaseControllerAsync;
import com.quantal.javashared.dto.CommonLogFields;
import com.quantal.javashared.dto.LogEvent;
import com.quantal.javashared.dto.LogzioConfig;
import com.quantal.javashared.logger.QuantalLogger;
import com.quantal.javashared.logger.QuantalLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * Created by dman on 08/03/2017.
 */

@RestController
@RequestMapping("/users/")
public class MicroserviceController extends BaseControllerAsync {

  private MicroserviceFacade microserviceFacade;
  //private Logger logger = LoggerFactory.getLogger(this.getClass());
  private QuantalLogger logger;

  @Autowired
  public MicroserviceController(MicroserviceFacade microserviceFacade,
                                CommonLogFields commonLogFields,
                                LogzioConfig logzioConfig) {
    this.microserviceFacade = microserviceFacade;
    logger = QuantalLoggerFactory.getLogzioLogger(this.getClass(), commonLogFields, logzioConfig);
  }

  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  @PostMapping(value="", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody MicroserviceDto microserviceDto){
    logger.debug("started to  create user ...", new LogEvent("USER_CREATE"));
    return microserviceFacade.saveOrUpdateUser(microserviceDto);
  }

  @GetMapping(value="")
  public CompletableFuture<String> getFunnyCatAsync(){
    logger.debug("started to  get funny cat ...", new LogEvent("USER_CREATE"));
    return microserviceFacade.getFunnyCat();
  }


}
