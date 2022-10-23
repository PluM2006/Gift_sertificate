package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.exception.ServiceException;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  private static URI getRedirectUri(String port, String port2, StringBuffer requestURL, UriBuilder uriBuilder) {
    return uriBuilder
        .path(replaceRedirectPort(port, port2, requestURL).replaceAll("http://", ""))
        .queryParam("redirect", true)
        .build();
  }

  private static String replaceRedirectPort(String port, String port2, StringBuffer requestURL) {
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
    String currentPort = String.valueOf(serverProperties.getPort());
    if (method.equals(HttpMethod.GET.name())) {
      long id = Long.parseLong(idParam);
      String redirectPort = serverProperties.getPortById(id);
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

    } else if (method.equals(HttpMethod.POST.name())) {
      String body = request.getReader().lines().collect(joining(System.lineSeparator()));
      List<Object> saveAll = serverProperties.getSourcePort().stream()
          .map(port -> CompletableFuture.supplyAsync(() -> webClient.post()
              .uri(uriBuilder -> getRedirectUri(currentPort, port.toString(), requestURL, uriBuilder))
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(body))
              .retrieve()
              .onStatus(HttpStatus::is4xxClientError,  resp ->
                  resp.bodyToMono(String.class)
                      .map(ServiceException::new))
              .bodyToMono(Object.class)
              .block())
          )
          .map(CompletableFuture::join)
          .collect(toList());
      String orderJson = mapper.writeValueAsString(saveAll.get(0));
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(orderJson);
    }

    return false;
  }

}
