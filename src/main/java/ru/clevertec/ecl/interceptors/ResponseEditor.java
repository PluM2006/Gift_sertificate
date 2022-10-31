package ru.clevertec.ecl.interceptors;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.clevertec.ecl.utils.Constants;

public class ResponseEditor {

  public static void changeResponse(HttpServletResponse response, String orderJson, HttpStatus status) {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(Constants.UTF8);
    response.setStatus(status.value());
    try {
      response.getWriter().write(orderJson);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
