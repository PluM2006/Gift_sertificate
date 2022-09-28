package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class ApiErrorDTO {

    HttpStatus status;
    String errorMessage;
    List<String> errors;

    public ApiErrorDTO(HttpStatus status, String errorMessage, List<String> errors) {
        super();
        this.status = status;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }

    public ApiErrorDTO(HttpStatus status, String errorMessage, String error) {
        super();
        this.status = status;
        this.errorMessage = errorMessage;
        errors = Collections.singletonList(error);
    }
}
