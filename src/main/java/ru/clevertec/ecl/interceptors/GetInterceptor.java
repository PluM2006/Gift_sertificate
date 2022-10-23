package ru.clevertec.ecl.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UriBuilder;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class GetInterceptor implements HandlerInterceptor {

//  private final String URL_BASE = "localhost:8080/api/v1/certificates/{id}";

  private final WebClient webClient;

  private final ServerProperties serverProperties;

  private final ObjectMapper mapper;

  private static URI getRedirectUri(String port, String port2, StringBuffer requestURL, UriBuilder uriBuilder) {
    return uriBuilder
        .path(getPath(port, port2, requestURL).replaceAll("http://", ""))
        .queryParam("redirect", true)
        .build();
  }

  private static String getPath(String port, String port2, StringBuffer requestURL) {
    return requestURL.toString().replaceAll(port, port2);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {

    System.out.println("postHandler " + request);
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ContentCachingRequestWrapper wrappedReq = (ContentCachingRequestWrapper) request;
    StringBuffer requestURL = new StringBuffer(wrappedReq.getRequestURL());
    Boolean redirect = Boolean.valueOf(request.getParameter("redirect"));
    String method = wrappedReq.getMethod();
    Map<?, ?> attribute = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String idParam = (String) attribute.get("id");
    if (redirect) {
      return true;
    }
    if (method.equals(HttpMethod.GET.name())) {
      long id = Long.parseLong(idParam);
      String redirectPort = serverProperties.getPortById(id);
      String currentPort = String.valueOf(serverProperties.getPort());
      if (currentPort.contains(redirectPort)) {
        return true;
      }
      Object block = webClient
          .get()
          .uri(uriBuilder -> getRedirectUri(currentPort, redirectPort, requestURL, uriBuilder))
          .retrieve()
          .bodyToMono(Object.class)
          .block();
      String orderJson = mapper.writeValueAsString(block);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(orderJson);

    }

    return false;
  }

}
