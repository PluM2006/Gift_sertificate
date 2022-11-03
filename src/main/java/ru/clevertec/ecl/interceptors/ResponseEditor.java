package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.dto.OrderDTO;

@Component
@RequiredArgsConstructor
public class ResponseEditor {

  private final ObjectMapper mapper;

  public void changeResponse(HttpServletResponse response, ResponseEntity<Object> responseEntity) {
    response.setStatus(responseEntity.getStatusCodeValue());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    try {
      response.getWriter().write(mapper.writeValueAsString(responseEntity.getBody()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void changeResponse(HttpServletResponse response, List<OrderDTO> orderDTOList) {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    try {
      response.getWriter().write(mapper.writeValueAsString(orderDTOList));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getBody(ContentCachingRequestWrapper request) {
    if (request.getContentAsByteArray().length != 0) {
      return new String(request.getContentAsByteArray());
    }
    try {
      return request.getReader().lines().collect(joining(System.lineSeparator()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
