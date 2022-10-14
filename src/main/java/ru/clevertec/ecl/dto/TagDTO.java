package ru.clevertec.ecl.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDTO {

  private Long id;

  @NotBlank
  private String name;
}
