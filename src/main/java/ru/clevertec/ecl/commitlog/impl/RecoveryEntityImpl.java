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
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.services.CertificateService;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecoveryCertificate implements RecoverEntity {

  @Override
  public void restoreEntity(List<CommitLog> commitLogCertificate, Class<?> c) {

  }

//  public void restoreEntity(List<CommitLog> commitLogCertificate) {
//    commitLogCertificate.stream()
//        .filter(s -> s.getOperation().equals(Operation.SAVE) || s.getOperation().equals(Operation.UPDATE))
//        .sorted(Comparator.comparing(CommitLog::getDateTimeOperation))
//        .forEach(this::saveOrUpdateCertificate);
//    commitLogCertificate.stream()
//        .filter(s -> s.getOperation().equals(Operation.DELETE))
//        .sorted(Comparator.comparing(CommitLog::getDateTimeOperation))
//        .forEach(this::deleteCertificate);
//  }
//
//  @SneakyThrows
//  private void saveOrUpdateCertificate(CommitLog commitLog) {
//    CertificateDTO save = certificateService.save(mapper.readValue(commitLog.getJson(), CertificateDTO.class));
//    log.info("Recovery from commitLog: save or update certificate {}", save);
//  }
//
//  @SneakyThrows
//  private void deleteCertificate(CommitLog commitLog) {
//    Long idDeleteCertificate = mapper.readValue(commitLog.getJson(), CertificateDTO.class).getId();
//    certificateService.delete(idDeleteCertificate);
//    log.info("Recovery from commitLog: delete certificate by id {}", idDeleteCertificate);
//  }

}
