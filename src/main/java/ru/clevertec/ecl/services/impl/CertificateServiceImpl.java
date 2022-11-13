package ru.clevertec.ecl.services.impl;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.entity.commitLog.Operation;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.commitLog.CommitLogService;
import ru.clevertec.ecl.services.TagService;
import ru.clevertec.ecl.utils.Constants;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificateServiceImpl implements CertificateService {

  private final CertificateRepository certificateRepository;
  private final TagService tagService;
  private final CertificateMapper certificateMapper;
  private final TagMapper tagMapper;
  private final CommitLogService commitLogService;
  @Lazy
  private final CertificateServiceImpl self;

  @Override
  public CertificateDTO getById(Long id) {
    return certificateRepository.findById(id)
        .map(certificateMapper::toCertificateDTO)
        .orElseThrow(() -> new EntityNotFoundException(Constants.CERTIFICATE, Constants.FIELD_NAME_ID, id));
  }

  @Override
  public List<CertificateDTO> getAll(Pageable pageable) {
    return certificateRepository
        .findAll(pageable).stream()
        .map(certificateMapper::toCertificateDTO)
        .collect(toList());
  }

  @Override
  public List<CertificateDTO> getByNameDescription(Pageable pageable, String name, String description) {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withMatcher(Constants.FIELD_NAME_NAME, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher(Constants.FIELD_NAME_DESCRIPTION, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
    return certificateRepository.findAll(
            Example.of(
                Certificate.builder()
                    .name(name)
                    .description(description).build(), exampleMatcher
            ), pageable, NamedEntityGraph.loading(Constants.ENTITY_GRAPH_NAME_CERTIFICATE_TAG)).stream()
        .map(certificateMapper::toCertificateDTO)
        .collect(toList());
  }

  @Override
  public CertificateDTO getByName(String name) {
    return certificateRepository.findByName(name)
        .map(certificateMapper::toCertificateDTO)
        .orElseThrow(() -> new EntityNotFoundException(Constants.CERTIFICATE, Constants.FIELD_NAME_NAME, name));
  }

  @Override
  public Set<CertificateDTO> getByTagsName(List<String> tagsNames, Pageable pageable) {
    return certificateRepository.findByTagsNameIgnoreCaseIsIn(tagsNames, pageable).stream()
        .map(certificateMapper::toCertificateDTO)
        .collect(toSet());
  }

  @Transactional
  @Override
  public CertificateDTO save(CertificateDTO certificateDTO) {
    Certificate certificate = certificateMapper.toCertificate(certificateDTO);
    certificate.setTags(tagService.saveAll(certificateDTO.getTags()).stream()
        .map(tagMapper::toTag)
        .collect(toList()));
    CertificateDTO saveCertificate = certificateMapper.toCertificateDTO(certificateRepository.save(certificate));
    log.info("CommitLog: save Certificate");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.SAVE, saveCertificate, Constants.CERTIFICATE);
    log.info("Result commitLog: save Certificate{}", commitLogService.write(commitLog));
    return saveCertificate;
  }

  @Transactional
  @Override
  public CertificateDTO update(Long id, CertificateDTO certificateDTO) {
    CertificateDTO updateCertificate = certificateRepository.findById(id)
        .map(certificate -> certificateMapper.certificateToUpdate(certificateDTO, certificate))
        .map(certificateMapper::toCertificateDTO)
        .map(self::save)
        .orElseThrow(() -> new EntityNotFoundException(Constants.CERTIFICATE, Constants.FIELD_NAME_ID, id));
    log.info("CommitLog: update Certificate");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.UPDATE, updateCertificate, Constants.CERTIFICATE);
    log.info("Result commitLog: update {}", commitLogService.write(commitLog));
    return updateCertificate;
  }

  @Transactional
  @Override
  public void delete(Long id) {
    Certificate deleteCertificate = certificateRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.CERTIFICATE, Constants.FIELD_NAME_ID, id));
    certificateRepository.delete(deleteCertificate);
    log.info("CommitLog: delete Certificate");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.DELETE,
        certificateMapper.toCertificateDTO(deleteCertificate), Constants.CERTIFICATE);
    log.info("Result commitLog: delete {}", commitLogService.write(commitLog));
  }
}
