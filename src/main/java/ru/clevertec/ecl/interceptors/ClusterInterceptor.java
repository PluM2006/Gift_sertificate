package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;
import javax.servlet.ServletException;
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
import reactor.core.publisher.Mono;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.ServiceException;
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
    boolean isRedirect = Boolean.parseBoolean(request.getParameter(Constants.REDIRECT));
    if (isRedirect) {
      return true;
    }
    String method = wrappedReq.getMethod();

    String currentPort = String.valueOf(serverProperties.getPort());
    if (method.equals(HttpMethod.GET.name())) {
      Map<?, ?> attribute = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
      String idParam = (String) attribute.get(Constants.FIELD_NAME_ID);
      if (idParam == null) {
        List<OrderDTO> orderDTOS = doFindAll(request);
        String orderJson = mapper.writeValueAsString(orderDTOS);
        ResponseEditor.changeResponse(response, orderJson, HttpStatus.OK);

      } else {
        OrderDTO order = doFindById(requestURL, idParam, currentPort);
        String orderJson = mapper.writeValueAsString(order);
        ResponseEditor.changeResponse(response, orderJson, HttpStatus.OK);
      }
    }

    if (method.equals(HttpMethod.POST.name())) {
      Object order = doPost(request, requestURL, currentPort);
      String orderJson = mapper.writeValueAsString(order);
      ResponseEditor.changeResponse(response, orderJson, HttpStatus.CREATED);
    }
    return false;
  }

  private Object doPost(HttpServletRequest request, StringBuffer requestURL, String currentPort) throws IOException {
    Long maxSequence = getMaxSequence();
    String redirectPort = serverProperties.getRedirectPort(maxSequence + 1);
    setSequenceVal(redirectPort, maxSequence);
    String body = request.getReader().lines().collect(joining(System.lineSeparator()));
    return webClient.post()
        .uri(UriEditor.getUriBuilderURIFunction(currentPort, redirectPort, requestURL))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(body))
        .retrieve()
        .bodyToMono(Object.class)
        .block();
  }

  private OrderDTO doFindById(StringBuffer requestURL, String idParam, String currentPort) {
    long id = Long.parseLong(idParam);
    String redirectPort = serverProperties.getRedirectPort(id);
    return webClient
        .get()
        .uri(UriEditor.getUriBuilderURIFunction(currentPort, redirectPort, requestURL))
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, resp ->
            resp.bodyToMono(String.class)
                .map(ServiceException::new))
        .bodyToMono(OrderDTO.class)
        .block();
  }

  private List<OrderDTO> doFindAll(HttpServletRequest request) {
    String page = request.getParameter(Constants.PAGE);
    String size = request.getParameter(Constants.SIZE);
    User user;
    try {
      user = mapper.readValue(request.getInputStream(), User.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    List<String> urlLimitOffset = UriEditor.buildLimitOffsetUrl(page, size, serverProperties.getSourcePort());
    return urlLimitOffset.stream()
        .map(s -> webClient.method(HttpMethod.GET)
            .uri(URI.create(s))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(user), User.class)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, resp ->
                resp.bodyToMono(String.class)
                    .map(ServletException::new))
            .bodyToMono(OrderDTO[].class)
            .block()
        ).filter(Objects::nonNull)
        .flatMap(Stream::of)
        .sorted(Comparator.comparing(OrderDTO::getId))
        .collect(toList());
  }

  private void setSequenceVal(String port, Long seq) {
    webClient.post()
        .uri(UriEditor.buildURINextSequence(port))
        .body(BodyInserters.fromValue(seq))
        .retrieve()
        .bodyToMono(Object.class)
        .block();
  }

  private Long getMaxSequence() {
    return serverProperties.getSourcePort().stream()
        .map(port -> webClient.get()
            .uri(UriEditor.buildURIMaxSequence(port))
            .retrieve()
            .bodyToMono(long.class)
            .block())
        .filter(Objects::nonNull)
        .max(Long::compareTo)
        .orElseThrow(NoSuchElementException::new);

  }

}
