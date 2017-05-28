package com.quantal.quantalmicroservicetemplate.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.quantal.quantalmicroservicetemplate.facades.MicroserviceFacade;
import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.jsonviews.MicroserviceViews;
import com.quantal.shared.controller.BaseControllerAsync;
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
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public MicroserviceController(MicroserviceFacade microserviceFacade) {
    this.microserviceFacade = microserviceFacade;
  }

  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  @PostMapping(value="", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody MicroserviceDto microserviceDto){
    logger.debug("started to  create user ...");
    return microserviceFacade.saveOrUpdateUser(microserviceDto);
  }

  @GetMapping(value="")
  public CompletableFuture<String> getFunnyCatAsync(){
    logger.debug("started to  get funny cat ...");
    return microserviceFacade.getFunnyCat();
  }


}
