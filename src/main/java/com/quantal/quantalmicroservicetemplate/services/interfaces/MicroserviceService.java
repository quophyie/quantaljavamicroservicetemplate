package com.quantal.quantalmicroservicetemplate.services.interfaces;

import com.quantal.quantalmicroservicetemplate.models.MicroserviceModel;

/**
 * Created by dman on 08/03/2017.
 */
public interface MicroserviceService {

  MicroserviceModel saveOrUpdate(MicroserviceModel microserviceModel);

}
