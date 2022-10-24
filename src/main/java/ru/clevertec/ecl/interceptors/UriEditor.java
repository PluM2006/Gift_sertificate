package ru.clevertec.ecl.interceptors;

import java.net.URI;
import java.util.function.Function;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.utils.Constants;

public class UriEditor {

  private final static String REQUEST_NEXT_SEQUENCE = "http://localhost:%s/api/v1/orders/sequence/next";

  private final static String REQUEST_LAST_VALUE_SEQUENCE = "http://localhost:%d/api/v1/orders/sequence/current";

  public static Function<UriBuilder, URI> getUriBuilderURIFunction(String port, String port2, StringBuffer requestURL) {
    return uriBuilder -> uriBuilder
        .path(replaceRedirectPort(port, port2, requestURL))
        .queryParam(Constants.REDIRECT, true).build();
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

}
