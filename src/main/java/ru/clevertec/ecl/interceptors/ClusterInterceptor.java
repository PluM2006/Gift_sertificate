package ru.clevertec.ecl.interceptors;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.utils.Constants;

@Slf4j
@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ClusterInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
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
            new ArrayList<>(serverProperties.getCluster().keySet()));
        List<OrderDTO> orderDTOList = responseEntityHandler.getOrderDTOResponseEntity(requestWrapper, urlLimitOffset)
            .stream()
            .flatMap(o -> Objects.requireNonNull(o.getBody()).stream())
            .sorted(Comparator.comparing(OrderDTO::getId))
            .collect(Collectors.toList());
        responseEditor.changeResponse(response, orderDTOList);

      } else {
        long id = Long.parseLong(idParam);
        Integer redirectPort = serverProperties.getRedirectPort(id);
        ResponseEntity<Object> object = responseEntityHandler.getObjectResponseEntity(requestWrapper,
            singletonList(redirectPort));
        responseEditor.changeResponse(response, object);
      }
    }

    if (method.equals(HttpMethod.POST.name())) {
      Long maxSequence = getMaxSequence();
      Integer redirectPort = serverProperties.getRedirectPort(maxSequence + 1);
      setSequenceVal(redirectPort, maxSequence);
      ResponseEntity<Object> object = responseEntityHandler.getObjectResponseEntity(requestWrapper,
          singletonList(redirectPort));
      responseEditor.changeResponse(response, object);
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {
    CachedBodyHttpServletRequest  requestWrapper = (CachedBodyHttpServletRequest ) request;
    boolean isRedirect = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REDIRECT)));
    int serverPort = request.getServerPort();
    List<Integer> portsReplica = serverProperties.getCluster().get(serverPort).stream()
        .filter(port -> serverPort != port).collect(
            Collectors.toList());

    if (isRedirect && requestWrapper.getMethod().equals(HttpMethod.POST.name())) {
      log.info("Отправляем так же реплики. Текущий порт {}. Нужно еще на {}", request.getServerPort(), portsReplica);
//      requestSender.doPostPutCommonEntityPost(request, webClient.post());
    }

    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

//
//  private List<OrderDTO> doFindAll(HttpServletRequest request) {
//    String page = request.getParameter(Constants.PAGE);
//    String size = request.getParameter(Constants.SIZE);
//    User user;
//    try {
//      user = mapper.readValue(request.getInputStream(), User.class);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//    List<String> urlLimitOffset = UriEditor.buildLimitOffsetUrl(page, size, serverProperties.getSourcePort());
//    return urlLimitOffset.stream()
//        .map(s -> webClient.method(HttpMethod.GET)
//            .uri(URI.create(s))
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(Mono.just(user), User.class)
//            .retrieve()
//            .onStatus(HttpStatus::is4xxClientError, resp ->
//                resp.bodyToMono(String.class)
//                    .map(ServletException::new))
//            .bodyToMono(OrderDTO[].class)
//            .block()
//        ).filter(Objects::nonNull)
//        .flatMap(Stream::of)
//        .sorted(Comparator.comparing(OrderDTO::getId))
//        .collect(toList());
//  }

  private void setSequenceVal(Integer port, Long seq) {
    webClient.post()
        .uri(uriEditor.buildURINextSequence(port.toString()))
        .body(BodyInserters.fromValue(seq))
        .retrieve()
        .bodyToMono(Object.class)
        .block();
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
