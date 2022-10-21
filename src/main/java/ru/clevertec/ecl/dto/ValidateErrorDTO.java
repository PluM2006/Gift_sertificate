package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ValidateErrorDTO {

  String object;
  String field;
  String code;
}
