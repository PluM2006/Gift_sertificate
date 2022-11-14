package ru.clevertec.ecl.commitlog.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.commitlog.Operation;
import ru.clevertec.ecl.commitlog.RecoverEntity;
import ru.clevertec.ecl.dto.AbstractDTO;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.OrderService;
import ru.clevertec.ecl.services.TagService;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecoveryEntityImpl implements RecoverEntity {

  private final CertificateService certificateService;
  private final TagService tagService;
  private final OrderService orderService;
  private final ObjectMapper mapper;

  @Override
  public void recoveryEntity(List<CommitLog> commitLog, Class<? extends AbstractDTO> classRecovery) {
    commitLog.stream()
        .filter(s -> s.getOperation().equals(Operation.SAVE) || s.getOperation().equals(Operation.UPDATE))
        .sorted(Comparator.comparing(CommitLog::getDateTimeOperation))
        .forEach(c -> saveOrUpdate(c, classRecovery));
    commitLog.stream()
        .filter(s -> s.getOperation().equals(Operation.DELETE))
        .sorted(Comparator.comparing(CommitLog::getDateTimeOperation))
        .forEach(c -> delete(c, classRecovery));
  }

  @SneakyThrows
  private void saveOrUpdate(CommitLog commitLog, Class<? extends AbstractDTO> classRecovery) {
    if (classRecovery.equals(CertificateDTO.class)) {
      CertificateDTO save = certificateService.save(mapper.readValue(commitLog.getJson(), CertificateDTO.class));
      log.info("Recovery from commitLog: save certificate by id {}", save);
    }
    if (classRecovery.equals(TagDTO.class)) {
      TagDTO save = tagService.save(mapper.readValue(commitLog.getJson(), TagDTO.class));
      log.info("Recovery from commitLog: save certificate by id {}", save);
    }
    if (classRecovery.equals(OrderDTO.class)) {
      OrderDTO orderDTO = mapper.readValue(commitLog.getJson(), OrderDTO.class);
      orderService.setSequence(orderDTO.getId() - 1);
      OrderDTO save = orderService.addOrder(orderDTO);
      log.info("Recovery from commitLog: save certificate by id {}", save);
    }

  }

  @SneakyThrows
  private void delete(CommitLog commitLog, Class<? extends AbstractDTO> classRecovery) {
    Long idDeleteCertificate = mapper.readValue(commitLog.getJson(), classRecovery).getId();
    if (classRecovery.isInstance(CertificateDTO.class)) {
      certificateService.delete(idDeleteCertificate);
    }
    if (classRecovery.isInstance(TagDTO.class)) {
      certificateService.delete(idDeleteCertificate);
    }
    log.info("Recovery from commitLog: delete certificate by id {}", idDeleteCertificate);
  }

}
