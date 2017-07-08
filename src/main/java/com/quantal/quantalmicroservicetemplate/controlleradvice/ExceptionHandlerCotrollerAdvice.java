package com.quantal.quantalmicroservicetemplate.controlleradvice;

import com.quantal.quantalmicroservicetemplate.constants.MessageCodes;
import com.quantal.shared.services.interfaces.MessageService;
import com.quantal.shared.util.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.HttpException;

import static com.quantal.shared.facades.AbstractBaseFacade.toRESTResponse;

/**
 * Created by dman on 08/07/2017.
 */
@RestController
@ControllerAdvice
public class ExceptionHandlerCotrollerAdvice {

    private Logger logger  = LogManager.getLogger();

    private MessageService messageService;

    @Autowired
    public ExceptionHandlerCotrollerAdvice(MessageService messageService) {
        this.messageService = messageService;
    }

   //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    //@ResponseBody
    public ResponseEntity handleThrowable(final Throwable ex) {
        logger.error("Unexpected error", ex);
        ResponseEntity responseEntity = toRESTResponse(null,
                messageService.getMessage(MessageCodes.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
        Exception businessEx = CommonUtils.extractBusinessException(ex);
     if (businessEx instanceof HttpException) {
            HttpStatus status = HttpStatus.valueOf(((HttpException) businessEx).code());
            responseEntity = toRESTResponse(null, businessEx.getMessage(), status);
        }
        logger.error(ex);
        return responseEntity;
    }


}
