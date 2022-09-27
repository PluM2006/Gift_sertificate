package ru.clevertec.ecl.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private String dtoName;
    private String fieldName;
    private Object fieldValue;
    private HttpStatus httpStatus;
    private Integer errorCode;

    public NotFoundException(String dtoName, String fieldName, Object fieldValue, HttpStatus httpStatus, Integer errorCode) {
        super(String.format("%s is not found with %s = %s", dtoName, fieldName, fieldValue));
        this.dtoName = dtoName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
