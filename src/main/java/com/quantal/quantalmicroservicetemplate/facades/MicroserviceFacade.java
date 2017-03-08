package com.quantal.quantalmicroservicetemplate.facades;

import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.models.MicroserviceModel;
import com.quantal.quantalmicroservicetemplate.services.interfaces.MicroserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by dman on 08/03/2017.
 */
@Service
public class MicroserviceFacade extends AbstractBaseFacade {

  private MicroserviceService microserviceService;

  @Autowired
  public MicroserviceFacade(MicroserviceService microserviceService) {
    this.microserviceService = microserviceService;
  }

  public ResponseEntity<?> saveOrUpdateUser(MicroserviceDto microserviceDto){

    MicroserviceModel microserviceModelToCreate = toModel(microserviceDto, MicroserviceModel.class);
    MicroserviceModel created  = microserviceService.saveOrUpdate(microserviceModelToCreate);
    MicroserviceDto createdDto = toDto(created, MicroserviceDto.class);
    return toRESTResponse(createdDto);
  }
}
