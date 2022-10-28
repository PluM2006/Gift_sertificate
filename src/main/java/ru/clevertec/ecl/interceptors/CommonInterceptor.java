package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.utils.Constants;

@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

  private final WebClient webClient;
  private final ServerProperties serverProperties;
  private final ObjectMapper mapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws IOException {
    ContentCachingRequestWrapper wrappedReq = (ContentCachingRequestWrapper) request;
    StringBuffer requestURL = new StringBuffer(wrappedReq.getRequestURL());
    boolean redirect = Boolean.parseBoolean(request.getParameter(Constants.REDIRECT));
    String method = wrappedReq.getMethod();
    if (redirect) {
      return true;
    }
    String currentPort = String.valueOf(serverProperties.getPort());
    if (HttpMethod.GET.name().equals(method)) {
      return true;
    }

    if (HttpMethod.POST.name().equals(method)) {
      doPost(request, webClient.post(), currentPort, requestURL, response);
    }

    if (HttpMethod.PUT.name().equals(method)) {
      doPost(request, webClient.put(), currentPort, requestURL, response);
    }

    if (HttpMethod.DELETE.name().equals(method)) {
      doDelete(response, requestURL, currentPort);
    }

    return false;
  }

  private void doPost(HttpServletRequest request, RequestBodyUriSpec webClient, String currentPort,
                      StringBuffer requestURL, HttpServletResponse response) throws IOException {
    String body = request.getReader().lines().collect(joining(System.lineSeparator()));
    List<Object> saveAll = serverProperties.getSourcePort().stream()
        .map(port -> CompletableFuture.supplyAsync(() -> webClient
            .uri(UriEditor.getUriBuilderURIFunction(currentPort, port.toString(), requestURL))
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
    ResponseEditor.changeResponse(response, orderJson);
  }

  private void doDelete(HttpServletResponse response, StringBuffer requestURL, String currentPort) {
    List<Object> collect = serverProperties.getSourcePort().stream()
        .map(port -> CompletableFuture.supplyAsync(() -> webClient.delete()
            .uri(UriEditor.getUriBuilderURIFunction(currentPort, port.toString(), requestURL))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, resp ->
                resp.bodyToMono(String.class)
                    .map(ServiceException::new))
            .bodyToMono(Object.class)
            .block())
        )
        .map(CompletableFuture::join)
        .collect(toList());
    response.setStatus(HttpStatus.NO_CONTENT.value());
  }

}
