package ru.clevertec.ecl.interceptors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.interceptors.response.ResponseEditor;
import ru.clevertec.ecl.interceptors.response.ResponseEntityHandler;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.ServerProperties;
import ru.clevertec.ecl.utils.cache.CachedBodyHttpServletRequest;

@Slf4j
@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ClusterInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final HealthCheckService healthCheckService;
  private final ServerProperties serverProperties;
  private final ResponseEntityHandler responseEntityHandler;
  private final ResponseEditor responseEditor;
  private final UriEditor uriEditor;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    CachedBodyHttpServletRequest requestWrapper = (CachedBodyHttpServletRequest) request;
    String method = requestWrapper.getMethod();
    boolean isRedirect = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REDIRECT)));
    if (isRedirect) {
      return true;
    }

    if (method.equals(HttpMethod.GET.name())) {
      Map<?, ?> attribute = (Map<?, ?>) requestWrapper.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
      String idParam = (String) attribute.get(Constants.FIELD_NAME_ID);
      if (idParam == null) {
        String page = requestWrapper.getParameter(Constants.PAGE);
        String size = requestWrapper.getParameter(Constants.SIZE);
        List<String> urlLimitOffset = uriEditor.buildLimitOffsetUrl(page, size,
            new ArrayList<>(healthCheckService.getWorkingClusterShards()));
        List<OrderDTO> orderDTOList = responseEntityHandler.getOrderDTOResponseEntity(requestWrapper, urlLimitOffset)
            .stream()
            .flatMap(o -> Objects.requireNonNull(o.getBody()).stream())
            .sorted(Comparator.comparing(OrderDTO::getId))
            .collect(toList());
        responseEditor.changeResponse(response, orderDTOList);

      } else {
        long id = Long.parseLong(idParam);
        Integer redirectShard = serverProperties.getRedirectShard(id);
        Integer redirectPort = serverProperties.getCluster().get(redirectShard).stream()
            .filter(healthCheckService::isWorking).findAny().orElseThrow(NoSuchElementException::new);
        ResponseEntity<Object> object = responseEntityHandler.getObjectResponseEntity(requestWrapper,
            singletonList(redirectPort));
        responseEditor.changeResponse(response, object);
      }
    }

    if (method.equals(HttpMethod.POST.name())) {
      Long maxSequence = getMaxSequence();
      Integer redirectShard = serverProperties.getRedirectShard(maxSequence + 1);
      Integer redirectPort = serverProperties.getCluster().get(redirectShard).stream()
          .filter(healthCheckService::isWorking).findAny().orElseThrow(NoSuchElementException::new);
      List<Integer> allPorts = serverProperties.getCluster()
          .entrySet().stream()
          .flatMap(o -> o.getValue().stream())
          .filter(healthCheckService::isWorking)
          .collect(toList());
      setSequenceVal(allPorts, maxSequence);
      ResponseEntity<Object> object = responseEntityHandler.getObjectResponseEntity(requestWrapper,
          singletonList(redirectPort));
      responseEditor.changeResponse(response, object);
    }
    return false;
  }

  private void setSequenceVal(List<Integer> ports, Long seq) {
    ports.forEach(port -> CompletableFuture.supplyAsync(() ->
        webClient.post()
            .uri(uriEditor.buildURINextSequence(port.toString()))
            .body(BodyInserters.fromValue(seq))
            .retrieve()
            .bodyToMono(Object.class)
            .block()));
  }

  private Long getMaxSequence() {
    return serverProperties.getCluster().keySet().stream()
        .map(port -> webClient.get()
            .uri(uriEditor.buildURIMaxSequence(port))
            .retrieve()
            .bodyToMono(long.class)
            .block())
        .filter(Objects::nonNull)
        .max(Long::compareTo)
        .orElseThrow(NoSuchElementException::new);

  }

}
