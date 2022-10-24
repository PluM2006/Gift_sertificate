package ru.clevertec.ecl.interceptors;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

public class ResponseEditor {

  public static void changeResponse(HttpServletResponse response, String orderJson) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(orderJson);
  }

}
