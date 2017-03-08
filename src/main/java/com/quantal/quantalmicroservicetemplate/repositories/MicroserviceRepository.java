package com.quantal.quantalmicroservicetemplate.repositories;

import com.quantal.quantalmicroservicetemplate.models.MicroserviceModel;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by dman on 08/03/2017.
 */
public interface MicroserviceRepository extends PagingAndSortingRepository<MicroserviceModel, Long> {
  MicroserviceModel findOneByEmail(String email);
}
