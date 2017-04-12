package com.quantal.quantalmicroservicetemplate.facades;

import com.quantal.basecomponents.facades.AbstractBaseFacade;
import com.quantal.basecomponents.objectmapper.NullSkippingOrikaBeanMapper;
import com.quantal.basecomponents.objectmapper.OrikaBeanMapper;
import com.quantal.quantalmicroservicetemplate.constants.MessageCodes;
import com.quantal.quantalmicroservicetemplate.dto.MicroserviceDto;
import com.quantal.quantalmicroservicetemplate.models.MicroserviceModel;
import com.quantal.quantalmicroservicetemplate.services.api.GiphyApiService;
import com.quantal.basecomponents.services.interfaces.MessageService;
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
  private MessageService messageService;


  @Autowired
  public MicroserviceFacade(MicroserviceService microserviceService,
                            GiphyApiService giphyApiService,
                            MessageService messageService,
                            OrikaBeanMapper orikaBeanMapper,
                            NullSkippingOrikaBeanMapper nullSkippingOrikaBeanMapper) {
    super(orikaBeanMapper, nullSkippingOrikaBeanMapper);
    this.microserviceService = microserviceService;
    this.giphyApiService = giphyApiService;
    this.messageService = messageService;
  }

  public ResponseEntity<?> saveOrUpdateUser(MicroserviceDto microserviceDto){

    MicroserviceModel microserviceModelToCreate = toModel(microserviceDto, MicroserviceModel.class);
    MicroserviceModel created  = microserviceService.saveOrUpdate(microserviceModelToCreate);
    MicroserviceDto createdDto = toDto(created, MicroserviceDto.class);
    return toRESTResponse(createdDto, messageService.getMessage(MessageCodes.ENTITY_CREATED, new String[]{MicroserviceModel.class.getSimpleName()}), HttpStatus.CREATED);
  }

  public ResponseEntity<?>  update(MicroserviceDto microserviceDto){

    String errMsg = "Model Not Found";
    if (microserviceDto == null) {
      return toRESTResponse(microserviceDto, errMsg, HttpStatus.NOT_FOUND);
    }

    MicroserviceModel modelInDB = microserviceService.findOneByEmail(microserviceDto.getEmail());

    if (modelInDB == null) {
      return toRESTResponse(microserviceDto, errMsg, HttpStatus.NOT_FOUND);
    }
    MicroserviceModel updatedModelToSave = toModel(microserviceDto, modelInDB, false);
    MicroserviceModel updated  = microserviceService.saveOrUpdate(updatedModelToSave);
    MicroserviceDto updatedDto = toDto(updated, MicroserviceDto.class);
    return toRESTResponse(updatedDto, messageService.getMessage(MessageCodes.ENTITY_UPDATED, new String[]{MicroserviceModel.class.getSimpleName()}));
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
