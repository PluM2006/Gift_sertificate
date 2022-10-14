package ru.clevertec.ecl.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.ecl.dto.ApiErrorDTO;
import ru.clevertec.ecl.dto.ValidateErrorDTO;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  private final static String VALID_EXCEPTION = "10005";

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
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                @NonNull HttpHeaders headers,
                                                                @NonNull HttpStatus status,
                                                                @NonNull WebRequest request) {
    List<String> errors = new ArrayList<>();
    List<ValidateErrorDTO> validateErrorDTOList = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
          errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
          validateErrorDTOList.add(getValidateErrorDTO(ex));
        }
    );
    ex.getBindingResult().getGlobalErrors().forEach(objectError ->
        errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage())
    );
    errors.add(VALID_EXCEPTION);

    ApiErrorDTO apiErrorDTO = ApiErrorDTO.builder()
        .status(status)
        .errorMessage(String.format("Validation filed. " + validateErrorDTOList.stream()
            .map(ValidateErrorDTO::toString)
            .collect(Collectors.joining())))
        .errors(errors).build();
    return handleExceptionInternal(ex, apiErrorDTO, headers, apiErrorDTO.getStatus(), request);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
    List<String> collect = Arrays.stream(ex.getCause().getLocalizedMessage().split("\\n"))
        .collect(Collectors.toList());
    ApiErrorDTO apiErrorDTO = new ApiErrorDTO(HttpStatus.BAD_REQUEST, collect.toString(), "40001");
    return new ResponseEntity<>(apiErrorDTO, new HttpHeaders(), apiErrorDTO.getStatus());
  }

  private static ValidateErrorDTO getValidateErrorDTO(MethodArgumentNotValidException ex) {
    return ValidateErrorDTO.builder()
        .object(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getObjectName())
        .code(ex.getBindingResult().getFieldError().getDefaultMessage())
        .field(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField())
        .build();
  }
}

