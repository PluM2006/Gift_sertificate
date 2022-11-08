package ru.clevertec.ecl.services.impl;

import java.net.URI;
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
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.ServerProperties;

@Service
@RequiredArgsConstructor
public class HealthCheckImpl implements HealthCheckService {

  private static final String URL_HEALTH = "http://localhost:%s/api/actuator/health";
  private final ServerProperties serverProperties;
  private final WebClient webClient;

  @Override
  public void checkHealthClusterNodes() {
    Map<Integer, List<Integer>> alivesPorts = new HashMap<>();
//    System.out.println(serverProperties.getCluster());

    for (Integer port: serverProperties.getCluster().keySet()){
      List<Integer> collect = serverProperties.getCluster().get(port).stream()
          .filter(p -> serviceHealth(p).getStatus().equals(Status.UP)).collect(Collectors.toList());
      alivesPorts.put(port, collect);
    }
    serverProperties.setClusterAliveNodes(alivesPorts);
//    serverProperties.getCluster().entrySet().stream()
//        .map(shard -> shard.getValue().stream()
//            .filter(port -> serviceHealth(port).getStatus().equals(Status.UP))
//            .collect(Collectors.toList())
//        );

//    System.out.println(alivesPorts);
  }

  private Health serviceHealth(Integer ports) {
    return webClient.get()
        .uri(URI.create(String.format(URL_HEALTH, ports)))
        .retrieve()
        .bodyToMono(Object.class)
        .map(s -> Health.up().build())
        .onErrorResume(ex -> Mono.just(Health.down().build())).block();
  }
}
