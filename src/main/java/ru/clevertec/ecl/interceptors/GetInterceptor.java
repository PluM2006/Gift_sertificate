package ru.clevertec.ecl.interceptors;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.clevertec.ecl.entity.Certificate;

@Component
@RequiredArgsConstructor
public class GetInterceptor implements HandlerInterceptor {

  private final String URL_BASE = "localhost:8080/api/v1/certificates/{id}";

  private final WebClient webClient;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {

    System.out.println("postHandler "+request);
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    System.out.println(request.getMethod());

    Map<?, ?> attribute = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String id = (String) attribute.get("id");
    Boolean parameter = Boolean.valueOf(request.getParameter("parr"));
    System.out.println(attribute.get("id"));
    System.out.println(parameter+" параметр");
    System.out.println(request.getContextPath());
    System.out.println(request.getRequestURI());
    if (parameter){
      return true;
    }
    System.out.println(request.getParameterMap().entrySet());

    Certificate block = webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path(URL_BASE)
            .queryParam("parr", true)
            .build(id)
        )
        .attribute("id", id)
        .attribute("parr", true)
        .retrieve().bodyToMono(Certificate.class)
        .block();
    System.out.println(block);
    return true;
  }
}
