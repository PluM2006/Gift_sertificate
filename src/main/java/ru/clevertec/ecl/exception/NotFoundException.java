package ru.clevertec.ecl.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private String dtoName;
    private String fieldName;
    private Object fieldValue;
    private HttpStatus httpStatus;
    private Integer errorCode;

    public NotFoundException(String dtoName, String fieldName, Object fieldValue, HttpStatus httpStatus, Integer errorCode) {
        super(MessageFormat.format("{0} is not found with {1} = {2}", dtoName, fieldName, fieldValue));
        this.dtoName = dtoName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
