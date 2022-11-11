package ru.clevertec.ecl.entity.commitLog;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.ServerProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataRecovery {

  private final static String URL = "localhost:%s/api/commitlog/current";
  private final ServerProperties serverProperties;
  private final HealthCheckService healthCheckService;
  private final WebClient webClient;

  @EventListener(ApplicationReadyEvent.class)
  public void restore() {
    Integer shard = serverProperties.getCluster().entrySet()
        .stream().filter(s -> s.getValue().contains(serverProperties.getPort()))
        .map(Entry::getKey)
        .findFirst().orElseThrow(NoSuchElementException::new);

    healthCheckService.checkHealthClusterNodes();
    List<Integer> ports = serverProperties.getClusterWorkingNodes().get(shard);
    while (ports.size() != 3) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      ports = serverProperties.getClusterWorkingNodes().get(shard);
      System.out.println(shard);
      log.info("White start all nodes. Start {} with 3", ports.size());
    }
    log.info("Start recovery date");
    Map<Integer, Integer> portSequence = new HashMap<>();
    List<Map<Integer, Integer>> collect = ports.stream()
        .map(s -> CompletableFuture.supplyAsync(() ->
            {

              Integer block = webClient.get()
                  .uri(URI.create(String.format(URL, s)))
                  .retrieve()
                  .bodyToMono(Integer.class)
                  .block();
              portSequence.put(s, block);
              return portSequence;
            }
        ))
        .map(CompletableFuture::join).collect(Collectors.toList());
    log.info("Sequence {}", portSequence);

  }

}
