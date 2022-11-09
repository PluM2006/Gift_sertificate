package ru.clevertec.ecl.utils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
  private Map<Integer, List<Integer>> cluster;

  private Map<Integer, List<Integer>> clusterWorkingNodes;

  public Integer getRedirectPort(Long value) {
    int portAllSize = cluster.size();
    long portIndex = value % portAllSize;
    return getPortFromMap(portIndex);
  }

  private Integer getPortFromMap(long portIndex) {
    return cluster.keySet().stream()
        .sorted().skip(portIndex)
        .limit(1)
        .findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

}
