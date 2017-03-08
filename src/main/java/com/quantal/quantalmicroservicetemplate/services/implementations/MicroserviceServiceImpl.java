package com.quantal.quantalmicroservicetemplate.services.implementations;

import com.quantal.quantalmicroservicetemplate.models.MicroserviceModel;
import com.quantal.quantalmicroservicetemplate.repositories.MicroserviceRepository;
import com.quantal.quantalmicroservicetemplate.services.interfaces.MicroserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by dman on 08/03/2017.
 */
@Service
public class MicroserviceServiceImpl implements MicroserviceService {

  private MicroserviceRepository microserviceRepository;

  @Autowired
  public MicroserviceServiceImpl(MicroserviceRepository microserviceRepository){
   this.microserviceRepository = microserviceRepository;
  }
  @Override
  public MicroserviceModel saveOrUpdate(MicroserviceModel microserviceModel) {
    microserviceModel.setJoinDate(LocalDate.now());
    return microserviceRepository.save(microserviceModel);
  }
}
