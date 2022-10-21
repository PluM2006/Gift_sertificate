package ru.clevertec.ecl.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EntityNotFoundException extends RuntimeException {

  private String dtoName;
  private String fieldName;
  private Object fieldValue;
  private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
  private Integer errorCode = 40004;

  public EntityNotFoundException(String dtoName, String fieldName, Object fieldValue) {
    super(String.format("%s is not found with %s = %s", dtoName, fieldName, fieldValue));
    this.dtoName = dtoName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }
}
