package ru.clevertec.ecl.interceptors;

import io.netty.handler.codec.http.HttpMethod;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.clevertec.ecl.interceptors.sender.RequestSender;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.ServerProperties;
import ru.clevertec.ecl.utils.cache.CachedBodyHttpServletRequest;

@Slf4j
@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ReplicaInterceptor implements HandlerInterceptor {

  private final ServerProperties serverProperties;
  private final HealthCheckService healthCheckService;
  private final RequestSender requestSender;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) {
    CachedBodyHttpServletRequest requestWrapper = (CachedBodyHttpServletRequest) request;
    int serverPort = request.getServerPort();

    boolean isReplicate = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REPLICATE)));
    if (isReplicate) {
      return;
    }
    boolean isRedirect = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REDIRECT)));
    List<Integer> portsReplica = serverProperties.getCluster().values()
        .stream()
        .filter(c -> c.contains(serverPort))
        .flatMap(Collection::stream)
        .filter(port -> serverPort != port)
        .filter(healthCheckService::isWorking)
        .collect(Collectors.toList());

    if (isRedirect && requestWrapper.getMethod().equals(HttpMethod.POST.name())) {
      requestWrapper.setAttribute(Constants.REPLICATE, String.valueOf(true));
      requestSender.forwardRequest(requestWrapper, portsReplica);
    }

  }

}
