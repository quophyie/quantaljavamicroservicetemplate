package com.quantal.quantalmicroservicetemplate.facades;

import com.quantal.javashared.annotations.logger.InjectLogger;
import com.quantal.javashared.dto.LogEvent;
import com.quantal.javashared.dto.LogTraceId;
import com.quantal.javashared.facades.AbstractBaseFacade;
import com.quantal.javashared.logger.QuantalLogger;
import com.quantal.javashared.objectmapper.NullSkippingOrikaBeanMapper;
import com.quantal.javashared.objectmapper.OrikaBeanMapper;
import com.quantal.javashared.services.interfaces.MessageService;
import com.quantal.quantalmicroservicetemplate.constants.MessageCodes;
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
  private MessageService messageService;

  //private Logger logger = LoggerFactory.getLogger(this.getClass())
  @InjectLogger
  private QuantalLogger logger;

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

    logger.debug("Saving user:", new LogEvent("USER_CREATE"), microserviceDto);
    MicroserviceModel microserviceModelToCreate = toModel(microserviceDto, MicroserviceModel.class);
    MicroserviceModel created  = microserviceService.saveOrUpdate(microserviceModelToCreate);
    MicroserviceDto createdDto = toDto(created, MicroserviceDto.class);
    logger.debug("Finished saving user:", new LogEvent("USER_CREATE"), createdDto);
    return toRESTResponse(createdDto, messageService.getMessage(MessageCodes.ENTITY_CREATED, new String[]{MicroserviceModel.class.getSimpleName()}), HttpStatus.CREATED);

  }

  public ResponseEntity<?>  update(MicroserviceDto microserviceDto){

    logger.debug("updating user:", new LogEvent("USER_UPDATE"),new LogTraceId("TRACE_ID"), microserviceDto);
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
    logger.debug("finished updating user:", new LogEvent("USER_UPDATE"),new LogTraceId("TRACE_ID"), updatedDto);
    return toRESTResponse(updatedDto, messageService.getMessage(MessageCodes.ENTITY_UPDATED, new String[]{MicroserviceModel.class.getSimpleName()}));
  }

  public CompletableFuture<String> getFunnyCat(){
    logger.debug("Getting funny cat", new LogEvent("FUNNY_CAT"),new LogTraceId("TRACE_ID"));
    //String result = "";
    //String result = giphyApiService.getGiphy("funny+cat", "dc6zaTOxFJmzC");
    CompletableFuture<String> result = giphyApiService.getGiphy("funny+cat", "dc6zaTOxFJmzC").thenApply((res) -> {
      logger.debug("finished getting funny cat", new LogEvent("FUNNY_CAT"));
      return res;
    });
    return result;
  }
}
