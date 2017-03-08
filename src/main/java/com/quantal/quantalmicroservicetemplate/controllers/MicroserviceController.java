package com.quantal.quantalmicroservicetemplate.controllers;

import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.facades.MicroserviceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dman on 08/03/2017.
 */

@RestController
@RequestMapping("/users/")
public class MicroserviceController {

  private MicroserviceFacade microserviceFacade;

  @Autowired
  public MicroserviceController(MicroserviceFacade microserviceFacade) {
    this.microserviceFacade = microserviceFacade;
  }

  @PostMapping(value="", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody MicroserviceDto microserviceDto){
    return microserviceFacade.saveOrUpdateUser(microserviceDto);
  }


}
