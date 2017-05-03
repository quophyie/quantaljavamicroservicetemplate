package com.quantal.quantalmicroservicetemplate.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.quantal.quantalmicroservicetemplate.facades.MicroserviceFacade;
import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.jsonviews.MicroserviceViews;
import com.quantal.shared.controller.BaseControllerAsync;
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

  @Autowired
  public MicroserviceController(MicroserviceFacade microserviceFacade) {
    this.microserviceFacade = microserviceFacade;
  }

  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  @PostMapping(value="", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody MicroserviceDto microserviceDto){
    return microserviceFacade.saveOrUpdateUser(microserviceDto);
  }

  @GetMapping(value="")
  public CompletableFuture<String> getFunnyCatAsync(){
    return microserviceFacade.getFunnyCat();
  }


}
