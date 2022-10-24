package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.utils.Constants;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ClusterInterceptor implements HandlerInterceptor {

  private final static String REQUEST_MAX_SEQUENCE = "http://localhost:%d/api/v1/orders/sequence/current";

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  private static String buildURIMaxSiquence(Integer port) {
    return String.format(REQUEST_MAX_SEQUENCE, port);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ContentCachingRequestWrapper wrappedReq = (ContentCachingRequestWrapper) request;
    StringBuffer requestURL = new StringBuffer(wrappedReq.getRequestURL());
    if (requestURL.toString().contains("sequence")) {
      return true;
    }
    boolean redirect = Boolean.parseBoolean(request.getParameter(Constants.REDIRECT));
    String method = wrappedReq.getMethod();
    Map<?, ?> attribute = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String idParam = (String) attribute.get(Constants.FIELD_NAME_ID);
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
      Object order = webClient
          .get()
          .uri(UriEditor.getUriBuilderURIFunction(currentPort, redirectPort, requestURL))
          .retrieve()
          .bodyToMono(Object.class)
          .block();
      String orderJson = mapper.writeValueAsString(order);
      ResponseEditor.changeResponse(response, orderJson);
    } else if (method.equals(HttpMethod.POST.name())) {

      Long maxSequence = getMaxSequence() + 1;
      System.out.println("maxSequence = "+maxSequence);
      String redirectPort = serverProperties.getPortById(maxSequence);
      System.out.println("redirectPort = "+redirectPort);
      if (currentPort.contains(redirectPort)) {
        return true;
      }
      String body = request.getReader().lines().collect(joining(System.lineSeparator()));
      Object order = webClient.post()
          .uri(UriEditor.getUriBuilderURIFunction(currentPort, redirectPort, requestURL))
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(body))
          .retrieve()
          .bodyToMono(Object.class)
          .block();
      String orderJson = mapper.writeValueAsString(order);
      ResponseEditor.changeResponse(response, orderJson);
    }

    return false;
  }

  private Long getMaxSequence() throws Exception {
    return serverProperties.getSourcePort().stream()
        .map(port -> CompletableFuture.supplyAsync(() -> webClient.get()
            .uri(buildURIMaxSiquence(port))
            .retrieve()
            .bodyToMono(long.class)
            .block()))
        .map(CompletableFuture::join)
        .max(Long::compareTo)
        .orElseThrow(Exception::new);

  }
}
