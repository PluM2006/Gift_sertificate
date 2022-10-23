package ru.clevertec.ecl.interceptors;

import java.net.URI;
import org.springframework.web.util.UriBuilder;

public class UriEditor {

  private final static String REDIRECT = "redirect";
  private final static String HTTP = "http://";

  public static URI getRedirectUri(String port, String port2, StringBuffer requestURL, UriBuilder uriBuilder) {
    return uriBuilder
        .path(replaceRedirectPort(port, port2, requestURL).replaceAll(HTTP, ""))
        .queryParam(REDIRECT, true)
        .build();
  }

  public static String replaceRedirectPort(String port, String port2, StringBuffer requestURL) {
    return requestURL.toString().replaceAll(port, port2);
  }

}
