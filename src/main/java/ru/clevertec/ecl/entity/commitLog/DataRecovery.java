package ru.clevertec.ecl.entity.commitLog;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.HealthCheckService;
import ru.clevertec.ecl.services.commitLog.CommitLogService;
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
  private final CommitLogService commitLogService;
  private final CertificateService certificateService;
  private final ObjectMapper mapper;

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
      List<CommitLog> recoveryData = webClient.get()
          .uri(URI.create(String.format(URL_RECOVERY_DATA, maxSequencePort.getKey(), limitData)))
          .retrieve()
          .bodyToFlux(CommitLog.class)
          .collect(Collectors.toList())
          .share()
          .block();

      List<CommitLog> commitLogCertificate = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.CERTIFICATE))
          .collect(Collectors.toList());
      List<CommitLog> commitLogTag = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.TAG))
          .collect(Collectors.toList());
      ;
      List<CommitLog> commitLogOrder = recoveryData.stream()
          .filter(s -> s.getEntityName().equals(Constants.ORDER))
          .collect(Collectors.toList());
      ;

      restoreCertificate(commitLogCertificate);
//      restoreTag(commitLogTag);
//      restoreOrder(commitLogOrder);

      log.info("Данные для восттановления {}", recoveryData);
    }

  }

  private void restoreCertificate(List<CommitLog> commitLogCertificate) {
    commitLogCertificate.stream()
        .filter(s -> s.getOperation().equals(Operation.SAVE) || s.getOperation().equals(Operation.UPDATE))
        .sorted(Comparator.comparing(CommitLog::getDateTimeOperation))
        .forEach(this::saveOrUpdateCertificate);
    commitLogCertificate.stream()
        .filter(s -> s.getOperation().equals(Operation.DELETE))
        .sorted(Comparator.comparing(CommitLog::getDateTimeOperation))
        .forEach(this::deleteCertificate);
  }

  @SneakyThrows
  private void saveOrUpdateCertificate(CommitLog commitLog) {
    certificateService.save(mapper.readValue(commitLog.getJson(), CertificateDTO.class));
  }

  @SneakyThrows
  private void deleteCertificate(CommitLog commitLog) {
    certificateService.delete(mapper.readValue(commitLog.getJson(), CertificateDTO.class).getId());
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
