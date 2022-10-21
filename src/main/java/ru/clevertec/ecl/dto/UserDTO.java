package ru.clevertec.ecl.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

  private Long id;
  private String firstName;
  private String secondName;

  @NotBlank
  private String username;
}
