package ru.clevertec.ecl.interceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.utils.Constants;

@Slf4j
@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

  private final ServerProperties serverProperties;
  private final ResponseEditor responseEditor;
  private final ResponseEntityHandler responseEntityHandler;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    CachedBodyHttpServletRequest  requestWrapper = (CachedBodyHttpServletRequest ) request;
    String method = requestWrapper.getMethod();
    List<Integer> ports = new ArrayList<>(serverProperties.getCluster().keySet());
    boolean isRedirect = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REDIRECT)));
    if (isRedirect) {
      return true;
    }

    if (HttpMethod.GET.name().equals(method)) {
      return true;
    }

    if (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method)) {
      ResponseEntity<Object> responseEntity = responseEntityHandler.getObjectResponseEntity(requestWrapper, ports);
      responseEditor.changeResponse(response, responseEntity);
    }

    if (HttpMethod.DELETE.name().equals(method)) {
      responseEntityHandler.getObjectResponseEntity(requestWrapper, ports);
      response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    return false;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {
    ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
    boolean isRedirect = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REDIRECT)));
    int serverPort = request.getServerPort();
    List<Integer> portsReplica = serverProperties.getCluster().get(serverPort).stream()
        .filter(port -> serverPort != port).collect(
            Collectors.toList());

    if (isRedirect) {
      log.info("Отправляем так же реплики. Текущий порт {}. Нужно еще на {}", request.getServerPort(), portsReplica);
//      requestSender.doPostPutCommonEntityPost(request, webClient.post());
    }

    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

}
