package ru.clevertec.ecl.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TagDTO {

  private Long id;

  @NotBlank
  private String name;
}
