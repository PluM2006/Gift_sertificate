package ru.clevertec.ecl.commitlog;

import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
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
import ru.clevertec.ecl.commitlog.impl.RecoveryEntityImpl;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.ServerProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataRecovery {

  private final static String URL_SEQUENCE = "localhost:%s/api/commitlog/current";
  private final static String URL_RECOVERY_DATA = "localhost:%s/api/commitlog/recovery/%d";
  private final ServerProperties serverProperties;
  private final HealthCheckService healthCheckService;
  private final WebClient webClient;
  private final RecoveryEntityImpl recoveryEntityImpl;

  @EventListener(ApplicationReadyEvent.class)
  public void restore() {
    Integer shard = getShards();
    healthCheckService.checkHealthClusterNodes();
    List<Integer> ports = serverProperties.getClusterWorkingNodes().get(shard);
    while (ports.size() != 3) {
      ports = serverProperties.getClusterWorkingNodes().get(shard);
    }
    Map<Integer, Integer> portSequence = getPortSequence(ports);
    Entry<Integer, Integer> maxSequencePort = portSequence.entrySet().stream()
        .max(Comparator.comparingInt(Entry::getValue))
        .orElseThrow(NoSuchElementException::new);
    Integer sequenceCurrenPort = portSequence.get(serverProperties.getPort());
    Integer sequenceRecoveryPort = maxSequencePort.getValue();
    if (sequenceCurrenPort < sequenceRecoveryPort) {
      log.info("Start recovery data");
      int limitData = sequenceRecoveryPort - sequenceCurrenPort;
      List<CommitLog> recoveryData = getRecoveryData(maxSequencePort, limitData);
      List<CommitLog> commitLogCertificate = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.CERTIFICATE))
          .collect(Collectors.toList());
      List<CommitLog> commitLogTag = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.TAG))
          .collect(Collectors.toList());
      List<CommitLog> commitLogOrder = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.ORDER))
          .collect(Collectors.toList());

      recoveryEntityImpl.recoveryEntity(commitLogCertificate, CertificateDTO.class);
      recoveryEntityImpl.recoveryEntity(commitLogOrder, OrderDTO.class);
      recoveryEntityImpl.recoveryEntity(commitLogTag, TagDTO.class);
    }

  }

  private List<CommitLog> getRecoveryData(Entry<Integer, Integer> maxSequencePort, int limitData) {
    return webClient.get()
        .uri(URI.create(String.format(URL_RECOVERY_DATA, maxSequencePort.getKey(), limitData)))
        .retrieve()
        .bodyToFlux(CommitLog.class)
        .collect(Collectors.toList())
        .share()
        .block();
  }

  private Integer getShards() {
    return serverProperties.getCluster().entrySet()
        .stream().filter(s -> s.getValue().contains(serverProperties.getPort()))
        .map(Entry::getKey)
        .findFirst().orElseThrow(NoSuchElementException::new);
  }

  private Map<Integer, Integer> getPortSequence(List<Integer> ports) {
    return ports.stream()
        .map(s -> CompletableFuture.supplyAsync(() ->
            {
              Integer block = webClient.get()
                  .uri(URI.create(String.format(URL_SEQUENCE, s)))
                  .retrieve()
                  .bodyToMono(Integer.class)
                  .block();
              return new SimpleEntry<>(s, block);
            }
        ))
        .map(CompletableFuture::join)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

}
