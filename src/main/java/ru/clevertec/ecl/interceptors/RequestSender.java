package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.utils.Constants;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestSender {

  private final ResponseEditor responseEditor;
  private final WebClient webClient;
  private final UriEditor uriEditor;

  public List<ResponseEntity<Object>> forwardRequest(ContentCachingRequestWrapper wrapperRequest, List<Integer> ports) {
    List<Function<UriBuilder, URI>> uriList = ports.stream()
        .map(port -> uriEditor.getUriBuilderURIFunction(wrapperRequest, port.toString())).collect(toList());
    return getListResponseEntity(wrapperRequest, uriList);
  }

  private List<ResponseEntity<Object>> getListResponseEntity(ContentCachingRequestWrapper wrapperRequest,
                                                             List<Function<UriBuilder, URI>> uriList) {
    return uriList.stream()
        .map(uri -> CompletableFuture.supplyAsync(() -> sendRequest(wrapperRequest, uri)))
        .map(CompletableFuture::join)
        .collect(toList());
  }

  private ResponseEntity<Object> sendRequest(ContentCachingRequestWrapper request,
                                             Function<UriBuilder, URI> uriBuilderURIFunction) {
    return webClient.method(HttpMethod.valueOf(request.getMethod()))
        .uri(uriBuilderURIFunction)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(responseEditor.getBody(request))
        .header(Constants.REDIRECT, String.valueOf(true))
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, resp ->
            resp.bodyToMono(String.class)
                .map(ServiceException::new))
        .toEntity(Object.class)
        .block();
  }

}
