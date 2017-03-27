package com.quantal.quantalmicroservicetemplate.models;

import com.quantal.quantalmicroservicetemplate.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by dman on 07/03/2017.
 */

@Entity
@Table(name = "microservice")
@Data
@ToString
@EqualsAndHashCode (of = {"id"})
public class MicroserviceModel implements Serializable{

  @Id
  @GeneratedValue
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
  private Gender gender;
}
