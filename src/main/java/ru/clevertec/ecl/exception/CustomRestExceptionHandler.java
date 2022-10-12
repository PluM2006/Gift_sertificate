package ru.clevertec.ecl.exception;

import lombok.NonNull;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.ecl.dto.ApiErrorDTO;
import ru.clevertec.ecl.dto.ValidateErrorDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String VALID_EXCEPTION = "10005";

    private static ValidateErrorDTO getValidateErrorDTO(MethodArgumentNotValidException ex) {
        return ValidateErrorDTO.builder()
                .object(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getObjectName())
                .code(ex.getBindingResult().getFieldError().getDefaultMessage())
                .field(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField())
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handlerNotFoundException(EntityNotFoundException exc, HttpServletResponse response) {
        return ResponseEntity.status(exc.getHttpStatus())
                .body(new ApiErrorDTO(exc.getHttpStatus(), exc.getMessage(), String.valueOf(exc.getErrorCode())));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handlerResponseStatusException(ResponseStatusException exc) {
        return ResponseEntity.status(exc.getStatus())
                .body(new ApiErrorDTO(exc.getStatus(), exc.getMessage(), String.valueOf(exc.getRawStatusCode())));
    }

    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        List<ValidateErrorDTO> validateErrorDTOList = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
            validateErrorDTOList.add(getValidateErrorDTO(ex));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        errors.add(VALID_EXCEPTION);
        ApiErrorDTO apiErrorDTO =
                new ApiErrorDTO(status,
                        String.format("Validation filed. " + validateErrorDTOList.stream()
                                .map(ValidateErrorDTO::toString)
                                .collect(Collectors.joining())), errors);
        return handleExceptionInternal(
                ex, apiErrorDTO, headers, apiErrorDTO.getStatus(), request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> collect =
                Arrays.stream(ex.getCause().getLocalizedMessage().split("\\n")).collect(Collectors.toList());
        ApiErrorDTO apiErrorDTO =
                new ApiErrorDTO(HttpStatus.BAD_REQUEST, collect.toString(), "40001");
        return new ResponseEntity<>(
                apiErrorDTO, new HttpHeaders(), apiErrorDTO.getStatus());
    }
}

