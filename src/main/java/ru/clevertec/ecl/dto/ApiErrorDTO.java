package ru.clevertec.ecl.dto;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
public class ApiErrorDTO {

  private HttpStatus status;
  private String errorMessage;
  private List<String> errors;

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
