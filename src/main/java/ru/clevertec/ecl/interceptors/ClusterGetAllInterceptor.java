package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import reactor.core.publisher.Mono;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.utils.Constants;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ClusterGetAllInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    boolean redirect = Boolean.parseBoolean(request.getParameter(Constants.REDIRECT));
    if (redirect) {
      return true;
    }
    String page = request.getParameter(Constants.PAGE);
    String size = request.getParameter(Constants.SIZE);
    User user = mapper.readValue(request.getInputStream(), User.class);
    List<String> urlLimitOffset = UriEditor.buildLimitOffsetUrl(page, size, serverProperties.getSourcePort());
    List<OrderDTO> collect = urlLimitOffset.stream()
        .map(s -> webClient.method(HttpMethod.GET)
            .uri(URI.create(s))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(user), User.class)
            .retrieve()
            .bodyToMono(OrderDTO[].class)
            .block()
        ).filter(Objects::nonNull)
        .flatMap(Stream::of)
        .sorted(Comparator.comparing(OrderDTO::getId))
        .collect(toList());

    String orderJson = mapper.writeValueAsString(collect);
    ResponseEditor.changeResponse(response, orderJson);
    return false;
  }
}
