package com.quantal.quantalmicroservicetemplate.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.quantal.quantalmicroservicetemplate.enums.Gender;
import com.quantal.quantalmicroservicetemplate.jsonviews.MicroserviceViews;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by dman on 08/03/2017.
 */
@Data
public class MicroserviceDto {
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private Long id;
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private String email;
  private String password;
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private String firstName;
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private String lastName;
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private LocalDate dob;
  private LocalDate joinDate;
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private LocalDate activeDate;
  private LocalDate deactivatedDate;
  private Long companyId;
  @JsonView(MicroserviceViews.MicroserviceCreatedView.class)
  private Gender gender;

}
