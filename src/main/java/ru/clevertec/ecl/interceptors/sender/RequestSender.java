package ru.clevertec.ecl.interceptors.sender;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.interceptors.UriEditor;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.cache.CachedBodyHttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestSender {

  private final WebClient webClient;
  private final UriEditor uriEditor;

  public List<ResponseEntity<Object>> forwardRequest(CachedBodyHttpServletRequest wrapperRequest, List<Integer> ports) {
    List<Function<UriBuilder, URI>> uriList = ports.stream()
        .map(port -> uriEditor.getUrlFunction(wrapperRequest, port.toString())).collect(toList());
    return uriList.stream()
        .map(uri -> CompletableFuture.supplyAsync(() -> webClientSend(wrapperRequest, uri)))
        .map(CompletableFuture::join)
        .collect(toList());
  }

  public List<ResponseEntity<List<OrderDTO>>> forwardRequestList(CachedBodyHttpServletRequest wrapperRequest,
                                                                 List<String> urls) {
    return urls.stream()
        .map(url -> CompletableFuture.supplyAsync(() -> webClientSend(wrapperRequest, url)))
        .map(CompletableFuture::join)
        .collect(toList());
  }

  private ResponseEntity<Object> webClientSend(CachedBodyHttpServletRequest wrapperRequest,
                                               Function<UriBuilder, URI> uriBuilderURIFunction) {
    return webClient.method(HttpMethod.valueOf(wrapperRequest.getMethod()))
        .uri(uriBuilderURIFunction)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(wrapperRequest.getReader().lines().collect(Collectors.joining()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(Constants.REDIRECT, String.valueOf(true))
        .header(Constants.REPLICATE, getReplicateAttribute(wrapperRequest))
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, resp ->
            resp.bodyToMono(String.class)
                .map(ServiceException::new))
        .toEntity(Object.class)
        .block();
  }

  private ResponseEntity<List<OrderDTO>> webClientSend(CachedBodyHttpServletRequest wrapperRequest, String url) {
    return webClient.method(HttpMethod.GET)
        .uri(URI.create(url))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(wrapperRequest.getReader().lines().collect(Collectors.joining()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(Constants.REDIRECT, String.valueOf(true))
        .header(Constants.REPLICATE, getReplicateAttribute(wrapperRequest))
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, resp ->
            resp.bodyToMono(String.class)
                .map(ServiceException::new))
        .toEntityList(OrderDTO.class)
        .block();
  }

  private String getReplicateAttribute(CachedBodyHttpServletRequest wrapperRequest) {
    return wrapperRequest.getAttribute(Constants.REPLICATE) == null ? String.valueOf(false)
        : wrapperRequest.getAttribute(Constants.REPLICATE).toString();
  }

}
