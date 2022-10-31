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
    boolean isRedirect = Boolean.parseBoolean(request.getParameter(Constants.REDIRECT));
    String method = wrappedReq.getMethod();
    if (isRedirect) {
      return true;
    }

    String currentPort = String.valueOf(serverProperties.getPort());
    if (HttpMethod.GET.name().equals(method)) {
      return true;
    }

    if (HttpMethod.POST.name().equals(method)) {
      Object entity = doPostPut(request, webClient.post(), currentPort, requestURL);
      ResponseEditor.changeResponse(response, mapper.writeValueAsString(entity), HttpStatus.CREATED);
    }

    if (HttpMethod.PUT.name().equals(method)) {
      Object entity = doPostPut(request, webClient.put(), currentPort, requestURL);
      ResponseEditor.changeResponse(response, mapper.writeValueAsString(entity), HttpStatus.OK);
    }

    if (HttpMethod.DELETE.name().equals(method)) {
      doDelete(requestURL, webClient.method(HttpMethod.DELETE), currentPort);
      response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    return false;
  }

  private Object doPostPut(HttpServletRequest request, RequestBodyUriSpec webClient, String currentPort,
                           StringBuffer requestURL) throws IOException {
    String body = request.getReader().lines().collect(joining(System.lineSeparator()));
    System.out.println(requestURL);
    return serverProperties.getSourcePort().stream()
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
        .collect(toList()).get(0);
  }

  private void doDelete(StringBuffer requestURL, RequestBodyUriSpec webClient, String currentPort) {
    List<Object> collect = serverProperties.getSourcePort().stream()
        .map(port -> CompletableFuture.supplyAsync(() -> webClient
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
  }

}
