package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@RequiredArgsConstructor
public class ResponseEditor {

  public void changeResponse(HttpServletResponse response, ResponseEntity<Object> responseEntity) {
    response.setStatus(responseEntity.getStatusCodeValue());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    try {
      response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
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
