package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import reactor.core.publisher.Mono;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.utils.Constants;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ClusterInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ContentCachingRequestWrapper wrappedReq = (ContentCachingRequestWrapper) request;
    StringBuffer requestURL = new StringBuffer(wrappedReq.getRequestURL());
    if (requestURL.toString().contains(Constants.SEQUENCE)) {
      return true;
    }
    boolean redirect = Boolean.parseBoolean(request.getParameter(Constants.REDIRECT));
    String method = wrappedReq.getMethod();
    Map<?, ?> attribute = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (redirect) {
      return true;
    }
    String idParam = (String) attribute.get(Constants.FIELD_NAME_ID);
    String currentPort = String.valueOf(serverProperties.getPort());

    if (method.equals(HttpMethod.GET.name())) {
      if (idParam==null) return true;
      long id = Long.parseLong(idParam);
      String redirectPort = serverProperties.getRedirectPort(id);
      System.out.println(serverProperties.getSourcePort());
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
      Long maxSequence = getMaxSequence();
      String redirectPort = serverProperties.getRedirectPort(maxSequence + 1);
      setSequenceVal(redirectPort, maxSequence);
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

  private void setSequenceVal(String port, Long seq) {
    webClient.post()
        .uri(UriEditor.buildURINextSequence(port))
        .body(BodyInserters.fromValue(seq))
        .retrieve()
        .bodyToMono(Object.class)
        .block();
  }

  private Long getMaxSequence() throws Exception {
    return serverProperties.getSourcePort().stream()
        .map(port -> webClient.get()
            .uri(UriEditor.buildURIMaxSequence(port))
            .retrieve()
            .bodyToMono(long.class)
            .block())
        .filter(Objects::nonNull)
        .max(Long::compareTo)
        .orElseThrow(Exception::new);
  }

}
