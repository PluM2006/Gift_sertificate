package ru.clevertec.ecl.interceptors;

import io.netty.handler.codec.http.HttpMethod;
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
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.ServerProperties;
import ru.clevertec.ecl.utils.cache.CachedBodyHttpServletRequest;

@Slf4j
@Component
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ReplicaInterceptor implements HandlerInterceptor {

  private final ServerProperties serverProperties;
  private final RequestSender requestSender;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) {
    int serverPort = request.getServerPort();
    List<Integer> ports = serverProperties.getCluster().get(serverPort);
    if (ports == null) {
      return;
    }
    CachedBodyHttpServletRequest requestWrapper = (CachedBodyHttpServletRequest) request;
    boolean isRedirect = Boolean.parseBoolean(String.valueOf(requestWrapper.getHeader(Constants.REDIRECT)));
    List<Integer> portsReplica = ports.stream()
        .filter(port -> serverPort != port).collect(
            Collectors.toList());

    if (isRedirect && requestWrapper.getMethod().equals(HttpMethod.POST.name())) {
      requestSender.forwardRequest(requestWrapper, portsReplica);
    }

  }

}
