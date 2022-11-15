package ru.clevertec.ecl.services.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.interceptors.UriEditor;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.ServerProperties;

@Service
@RequiredArgsConstructor
public class HealthCheckImpl implements HealthCheckService {

  private static final int TIMEOUT = 3000;
  private final ServerProperties serverProperties;
  private final WebClient webClient;
  private final UriEditor uriEditor;

  @Override
  public void checkHealthClusterNodes() {
    Map<Integer, List<Integer>> workingNodes = new HashMap<>();
    for (Integer port : serverProperties.getCluster().keySet()) {
      List<Integer> collect = serverProperties.getCluster().get(port).stream()
          .filter(p -> checkHealthNode(p).getStatus().equals(Status.UP)).collect(Collectors.toList());
      workingNodes.put(port, collect);
    }
    serverProperties.setClusterWorkingNodes(workingNodes);
  }

  @Override
  public boolean isWorking(Integer port) {
    return checkHealthNode(port).getStatus().equals(Status.UP);
  }

  @Override
  public List<Integer> getWorkingClusterShards() {
    return serverProperties.getCluster().values().stream()
        .map(l -> l.stream().filter(this::isWorking)
            .findFirst()
            .orElseThrow(() -> new ServiceException(String.format("One of the shards is not working %s", l)))
        ).collect(Collectors.toList());
  }

  private Health checkHealthNode(Integer port) {
    return webClient.get()
        .uri(uriEditor.buildURIHealth(port))
        .retrieve()
        .bodyToMono(Object.class)
        .timeout(Duration.ofMillis(TIMEOUT))
        .map(s -> Health.up().build())
        .onErrorResume(ex -> Mono.just(Health.down().build())).block();
  }
}
