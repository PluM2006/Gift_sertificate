package ru.clevertec.ecl.dmain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class ApiErrorDTO {
    private HttpStatus status;
    private String errorMessage;
    private List<String> errors;

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
