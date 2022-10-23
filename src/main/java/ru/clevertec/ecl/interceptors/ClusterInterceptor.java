package ru.clevertec.ecl.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ClusterInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


//    if (method.equals(HttpMethod.GET.name())) {
//      long id = Long.parseLong(idParam);
//      String redirectPort = serverProperties.getPortById(id);
//      if (currentPort.contains(redirectPort)) {
//        return true;
//      }
//      Object block = webClient
//          .get()
//          .uri(uriBuilder -> getRedirectUri(currentPort, redirectPort, requestURL, uriBuilder))
//          .retrieve()
//          .bodyToMono(Object.class)
//          .block();
//      String orderJson = mapper.writeValueAsString(block);
//      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//      response.setCharacterEncoding("UTF-8");
//      response.getWriter().write(orderJson);
//
//    }

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
