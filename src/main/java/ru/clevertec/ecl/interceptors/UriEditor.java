package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.utils.Constants;

public class UriEditor {

  private final static String REQUEST_NEXT_SEQUENCE = "http://localhost:%s/api/v1/orders/sequence/set";
  private final static String REQUEST_LAST_VALUE_SEQUENCE = "http://localhost:%d/api/v1/orders/sequence/current";
  private static final String URL_OFFSET_LIMIT_REQUEST = "http://localhost:%d/api/v1/orders?limit=%d&offset=%d&redirect=true";

  public static Function<UriBuilder, URI> getUriBuilderURIFunction(String port, String port2, StringBuffer requestURL) {
    return uriBuilder -> uriBuilder
        .path(replaceRedirectPort(port, port2, requestURL))
        .queryParam(Constants.REDIRECT, true).build();
  }

  public static List<String> buildLimitOffsetUrl(Integer page, Integer size, List<Integer> sourcePort) {
    if (page == 0) {
      page = 1;
    }
    int currentMaxElement = page * size;
    int currentMinElement = currentMaxElement - size;
    int remainder = page * size - size;
    Integer divider = sourcePort.size();
    return IntStream.range(0, sourcePort.size())
        .mapToObj(i -> {
          long limit = countModIndex(currentMinElement + 1, currentMaxElement + 1, i, divider);
          long offset = countModIndex(1, remainder + 1, i, divider);
          return String.format(URL_OFFSET_LIMIT_REQUEST, sourcePort.get(i), limit, offset);
        }).collect(toList());
  }

  public static String replaceRedirectPort(String port, String port2, StringBuffer requestURL) {
    return requestURL.toString().replaceAll(port, port2).replaceAll(Constants.HTTP, "");
  }

  public static String buildURINextSequence(String port) {
    return String.format(REQUEST_NEXT_SEQUENCE, port);
  }

  public static String buildURIMaxSequence(Integer port) {
    return String.format(REQUEST_LAST_VALUE_SEQUENCE, port);
  }

  private static long countModIndex(int start, int end, int index, Integer divider) {
    return IntStream.range(start, end)
        .filter(value1 -> value1 % divider == index)
        .count();
  }

}
