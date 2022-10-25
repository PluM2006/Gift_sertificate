package ru.clevertec.ecl.interceptors;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
@ConfigurationProperties(prefix = "server")
public class ServerProperties {

  private String host;
  private int port;
  private List<Integer> sourcePort;

  public String getRedirectPort(Long value) {
    int portAllSize = sourcePort.size();
    long portIndex = value % portAllSize;
    return sourcePort.get((int) portIndex).toString();

  }

}
