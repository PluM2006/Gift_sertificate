package ru.clevertec.ecl.interceptors;

import java.util.ArrayList;
import java.util.List;
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
public class CommonInterceptor implements HandlerInterceptor {

  private final ServerProperties serverProperties;
  private final ResponseEditor responseEditor;
  private final ResponseEntityHandler responseEntityHandler;
  private final HealthCheckService healthCheckService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    CachedBodyHttpServletRequest requestWrapper = (CachedBodyHttpServletRequest) request;
    String method = requestWrapper.getMethod();
//    List<Integer> ports = new ArrayList<>(serverProperties.getCluster().keySet());
    List<Integer> ports = healthCheckService.getWorkingClusterShards();
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

}
