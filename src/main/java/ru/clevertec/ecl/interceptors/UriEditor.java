package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.PageSize;
import ru.clevertec.ecl.utils.ServerProperties;

@Component
@RequiredArgsConstructor
public class UriEditor {

  private final static String REQUEST_NEXT_SEQUENCE = "http://%s:%s/api/v1/orders/sequence/set";
  private final static String REQUEST_LAST_VALUE_SEQUENCE = "http://%s:%d/api/v1/orders/sequence/current";
  private static final String URL_OFFSET_LIMIT_REQUEST = "http://%s:%d/api/v1/orders/offset?limit=%d&offset=%d";
  private final ServerProperties serverProperties;

  private static long countModIndex(int start, int end, int index, Integer divider) {
    return IntStream.range(start, end)
        .filter(element -> element % divider == index)
        .count();
  }

  public Function<UriBuilder, URI> getUrlFunction(HttpServletRequest request, String redirectPort) {
    return uriBuilder -> uriBuilder
        .scheme(request.getScheme())
        .host(serverProperties.getHost())
        .port(redirectPort)
        .path(request.getContextPath())
        .path(request.getServletPath())
        .query(request.getQueryString())
        .build();
  }

  public List<String> buildLimitOffsetUrl(String stringPage, String stringSize, List<Integer> sourcePort) {
    PageSize pageSize = new PageSize(stringPage, stringSize);
    int currentMaxElement = pageSize.getPage() * pageSize.getSize();
    int currentMinElement = currentMaxElement - pageSize.getSize();
    int remainder = pageSize.getPage() * pageSize.getSize() - pageSize.getSize();
    Integer divider = sourcePort.size();
    return IntStream.range(0, sourcePort.size())
        .mapToObj(i -> {
          long limit = countModIndex(currentMinElement + 1, currentMaxElement + 1, i, divider);
          long offset = countModIndex(1, remainder + 1, i, divider);
          return String.format(URL_OFFSET_LIMIT_REQUEST, serverProperties.getHost(), sourcePort.get(i), limit, offset);
        })
        .filter(s -> !s.contains(Constants.LIMIT_ZERO))
        .collect(toList());
  }

  public String buildURINextSequence(String port) {
    return String.format(REQUEST_NEXT_SEQUENCE, serverProperties.getHost(), port);
  }

  public String buildURIMaxSequence(Integer port) {
    return String.format(REQUEST_LAST_VALUE_SEQUENCE, serverProperties.getHost(), port);
  }

}
