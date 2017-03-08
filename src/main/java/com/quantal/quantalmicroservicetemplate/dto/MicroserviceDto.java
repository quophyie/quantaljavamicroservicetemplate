package com.quantal.quantalmicroservicetemplate.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * Created by dman on 08/03/2017.
 */
@Data
public class MicroserviceDto {
  private Long id;

  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private LocalDate joinDate;
  private LocalDate activeDate;
  private LocalDate deactivatedDate;
  private Long companyId;

}
