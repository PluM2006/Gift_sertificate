package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.exception.ServiceException;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ContentCachingRequestWrapper wrappedReq = (ContentCachingRequestWrapper) request;
    StringBuffer requestURL = new StringBuffer(wrappedReq.getRequestURL());
    boolean redirect = Boolean.parseBoolean(request.getParameter("redirect"));
    String method = wrappedReq.getMethod();
    Map<?, ?> attribute = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String idParam = (String) attribute.get("id");
    if (redirect) {
      return true;
    }
    String currentPort = String.valueOf(serverProperties.getPort());
    if (method.equals(HttpMethod.GET.name())) {
      return true;

    } else if (method.equals(HttpMethod.POST.name())) {
      String body = request.getReader().lines().collect(joining(System.lineSeparator()));
      List<Object> saveAll = serverProperties.getSourcePort().stream()
          .map(port -> CompletableFuture.supplyAsync(() -> webClient.post()
              .uri(uriBuilder -> UriEditor.getRedirectUri(currentPort, port.toString(), requestURL, uriBuilder))
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(body))
              .retrieve()
              .onStatus(HttpStatus::is4xxClientError, resp ->
                  resp.bodyToMono(String.class)
                      .map(ServiceException::new))
              .bodyToMono(Object.class)
              .block())
          )
          .map(CompletableFuture::join)
          .collect(toList());
      String orderJson = mapper.writeValueAsString(saveAll.get(0));
      changeResponse(response, orderJson);
    } else if (method.equals(HttpMethod.PUT.name())) {
      String body = request.getReader().lines().collect(joining(System.lineSeparator()));
      List<Object> saveAll = serverProperties.getSourcePort().stream()
          .map(port -> CompletableFuture.supplyAsync(() -> webClient.put()
              .uri(uriBuilder -> UriEditor.getRedirectUri(currentPort, port.toString(), requestURL, uriBuilder))
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(body))
              .retrieve()
              .onStatus(HttpStatus::is4xxClientError, resp ->
                  resp.bodyToMono(String.class)
                      .map(ServiceException::new))
              .bodyToMono(Object.class)
              .block())
          )
          .map(CompletableFuture::join)
          .collect(toList());
      String orderJson = mapper.writeValueAsString(saveAll.get(0));
      changeResponse(response, orderJson);
    }

    return false;
  }

  private void changeResponse(HttpServletResponse response, String orderJson) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(orderJson);
  }

}
