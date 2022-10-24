package ru.clevertec.ecl.interceptors;

import java.net.URI;
import java.util.function.Function;
import org.springframework.web.util.UriBuilder;
import ru.clevertec.ecl.utils.Constants;

public class UriEditor {

  public static Function<UriBuilder, URI> getUriBuilderURIFunction(String port, String port2, StringBuffer requestURL) {
    return uriBuilder -> uriBuilder
        .path(replaceRedirectPort(port, port2, requestURL))
        .queryParam(Constants.REDIRECT, true).build();
  }

  public static String replaceRedirectPort(String port, String port2, StringBuffer requestURL) {
    return requestURL.toString().replaceAll(port, port2).replaceAll(Constants.HTTP, "");
  }

}
