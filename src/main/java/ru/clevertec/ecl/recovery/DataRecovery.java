package ru.clevertec.ecl.recovery;

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
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.interceptors.UriEditor;
import ru.clevertec.ecl.recovery.impl.RecoveryEntityImpl;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.ServerProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataRecovery {

  private final ServerProperties serverProperties;
  private final HealthCheckService healthCheckService;
  private final WebClient webClient;
  private final RecoveryEntityImpl recoveryEntityImpl;
  private final UriEditor uriEditor;

  @EventListener(ApplicationReadyEvent.class)
  public void restore() {
    Integer shard = getShards();
    healthCheckService.checkHealthClusterNodes();
    List<Integer> ports = serverProperties.getClusterWorkingNodes().get(shard);
    int i = 0;
    while (ports.size() != 3) {
      ports = serverProperties.getClusterWorkingNodes().get(shard);
      i++;
      if (i == 10000) {
        return;
      }
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
          .filter(s -> s.getEntityName().equals(Constants.CERTIFICATE)).collect(Collectors.toList());
      List<CommitLog> commitLogTag = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.TAG)).collect(Collectors.toList());
      List<CommitLog> commitLogOrder = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.ORDER)).collect(Collectors.toList());

      recoveryEntityImpl.recoveryEntity(commitLogCertificate, CertificateDTO.class);
      recoveryEntityImpl.recoveryEntity(commitLogOrder, OrderDTO.class);
      recoveryEntityImpl.recoveryEntity(commitLogTag, TagDTO.class);
    }
  }

  private List<CommitLog> getRecoveryData(Entry<Integer, Integer> port, int limitData) {
    return webClient.get()
        .uri(uriEditor.buildUrlRecoveryDate(port.getKey(), limitData))
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
        .map(port -> CompletableFuture.supplyAsync(() ->
            {
              Integer block = webClient.get()
                  .uri(uriEditor.buildUrlSequenceCommitLog(port))
                  .retrieve()
                  .bodyToMono(Integer.class)
                  .block();
              return new SimpleEntry<>(port, block);
            }
        ))
        .map(CompletableFuture::join)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

}
