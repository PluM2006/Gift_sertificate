package ru.clevertec.ecl.interceptors;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.PageSize;

public class UriEditor {

  private final static String REQUEST_NEXT_SEQUENCE = "http://localhost:%s/api/v1/orders/sequence/set";
  private final static String REQUEST_LAST_VALUE_SEQUENCE = "http://localhost:%d/api/v1/orders/sequence/current";
  private static final String URL_OFFSET_LIMIT_REQUEST = "http://localhost:%d/api/v1/orders/offset?limit=%d&offset=%d&redirect=true";

  public static Function<UriBuilder, URI> getUriBuilderURIFunction(String currentPort, String redirectPort,
                                                                   StringBuffer requestURL) {
    return uriBuilder -> uriBuilder
        .path(replaceRedirectPort(currentPort, redirectPort, requestURL))
        .queryParam(Constants.REDIRECT, true)
        .queryParam("replicate", redirectPort)
        .build();

  }

  public static List<String> buildLimitOffsetUrl(String stringPage, String stringSize, List<Integer> sourcePort) {
    PageSize pageSize = new PageSize(stringPage, stringSize);
    int currentMaxElement = pageSize.getPage() * pageSize.getSize();
    int currentMinElement = currentMaxElement - pageSize.getSize();
    int remainder = pageSize.getPage() * pageSize.getSize() - pageSize.getSize();
    Integer divider = sourcePort.size();
    return IntStream.range(0, sourcePort.size())
        .mapToObj(i -> {
          long limit = countModIndex(currentMinElement + 1, currentMaxElement + 1, i, divider);
          long offset = countModIndex(1, remainder + 1, i, divider);
          return String.format(URL_OFFSET_LIMIT_REQUEST, sourcePort.get(i), limit, offset);
        })
        .filter(s -> !s.contains(Constants.LIMIT_ZERO))
        .collect(toList());
  }

  public static String replaceRedirectPort(String currentPort, String redirectPort, StringBuffer requestURL) {
    return requestURL.toString().replaceAll(currentPort, redirectPort).replaceAll(Constants.HTTP, "");
  }

  public static String buildURINextSequence(String port) {
    return String.format(REQUEST_NEXT_SEQUENCE, port);
  }

  public static String buildURIMaxSequence(Integer port) {
    return String.format(REQUEST_LAST_VALUE_SEQUENCE, port);
  }

  private static long countModIndex(int start, int end, int index, Integer divider) {
    return IntStream.range(start, end)
        .filter(element -> element % divider == index)
        .count();
  }

}
