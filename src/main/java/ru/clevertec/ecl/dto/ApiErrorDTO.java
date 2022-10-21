package ru.clevertec.ecl.dto;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ApiErrorDTO {

  HttpStatus status;
  String errorMessage;
  List<String> errors;

  public ApiErrorDTO(HttpStatus status, String errorMessage, List<String> errors) {
    this.status = status;
    this.errorMessage = errorMessage;
    this.errors = errors;
  }

  public ApiErrorDTO(HttpStatus status, String errorMessage, String error) {
    this.status = status;
    this.errorMessage = errorMessage;
    errors = Collections.singletonList(error);
  }
}
