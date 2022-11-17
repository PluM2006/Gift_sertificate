package ru.clevertec.ecl.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class UserDTO {

  private Long id;
  private String firstName;
  private String secondName;

  @NotBlank
  private String username;
}
