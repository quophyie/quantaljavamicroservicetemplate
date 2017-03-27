package com.quantal.quantalmicroservicetemplate.facades;

import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.models.MicroserviceModel;
import com.quantal.quantalmicroservicetemplate.services.api.GiphyApiService;
import com.quantal.quantalmicroservicetemplate.services.interfaces.MicroserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Created by dman on 08/03/2017.
 */
@Service
public class MicroserviceFacade extends AbstractBaseFacade {

  private MicroserviceService microserviceService;
  private final GiphyApiService giphyApiService;


  @Autowired
  public MicroserviceFacade(MicroserviceService microserviceService, GiphyApiService giphyApiService) {
    this.microserviceService = microserviceService;
    this.giphyApiService = giphyApiService;
  }

  public ResponseEntity<?> saveOrUpdateUser(MicroserviceDto microserviceDto){

    MicroserviceModel microserviceModelToCreate = toModel(microserviceDto, MicroserviceModel.class);
    MicroserviceModel created  = microserviceService.saveOrUpdate(microserviceModelToCreate);
    MicroserviceDto createdDto = toDto(created, MicroserviceDto.class);
    return toRESTResponse(createdDto);
  }

  public ResponseEntity<?>  update(MicroserviceDto microserviceDto){

    if (microserviceDto == null) {
      return toRESTResponse(microserviceDto, HttpStatus.NOT_FOUND);
    }

    MicroserviceModel modelInDB = microserviceService.findOneByEmail(microserviceDto.getEmail());

    if (modelInDB == null) {
      return toRESTResponse(microserviceDto, HttpStatus.NOT_FOUND);
    }
    MicroserviceModel updatedModelToSave = toModel(microserviceDto, modelInDB, false);
    MicroserviceModel updated  = microserviceService.saveOrUpdate(updatedModelToSave);
    MicroserviceDto updatedDto = toDto(updated, MicroserviceDto.class);
    return toRESTResponse(updatedDto);
  }

  public CompletableFuture<String> getFunnyCat(){
    //String result = "";
    //String result = giphyApiService.getGiphy("funny+cat", "dc6zaTOxFJmzC");
    CompletableFuture<String> result = giphyApiService.getGiphy("funny+cat", "dc6zaTOxFJmzC").thenApply((res) -> {
      return res;
    });
    return result;
  }
}
