package ru.clevertec.ecl.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.data.CertificateTestData;
import ru.clevertec.ecl.data.TagTestData;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.TagService;
import ru.clevertec.ecl.services.commitLog.CommitLogService;
import ru.clevertec.ecl.utils.Constants;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

  @InjectMocks
  private CertificateServiceImpl certificateService;

  @Mock
  private CommitLogService commitLogService;

  @Mock
  private CertificateServiceImpl self;

  @Mock
  private CertificateRepository certificateRepository;

  @Mock
  private CertificateMapper certificateMapper;

  @Mock
  private TagService tagService;

  @Mock
  private TagMapper tagMapper;
  private CertificateDTO certificateDTO;
  private Certificate certificate;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    certificate = CertificateTestData.buildCertificateOne();
    certificateDTO = CertificateTestData.buildCertificateDTO();
    pageable = PageRequest.of(1, 2, Sort.by("name, desc"));
  }

  @Test
  void saveSuccessTest() {
    given(certificateRepository.save(any())).willReturn(certificate);
    given(certificateMapper.toCertificateDTO(any())).willReturn(certificateDTO);
    given(certificateMapper.toCertificate(any())).willReturn(certificate);
    CertificateDTO saveCertificateDTO = certificateService.save(certificateDTO);
    assertThat(saveCertificateDTO).isNotNull();
  }

  @Test
  void updateSuccessTest() {
    given(certificateMapper.toCertificateDTO(certificate)).willReturn(certificateDTO);
    given(certificateMapper.certificateToUpdate(any(), any())).willReturn(certificate);
    given(self.save(certificateDTO)).willReturn(certificateDTO);
    given(certificateRepository.findById(any())).willReturn(Optional.of(certificate));
    certificateDTO.setName("Certificate 2");
    CertificateDTO update = certificateService.update(1L, certificateDTO);
    assertAll(() -> assertThat(update).isNotNull(),
        () -> assertEquals(certificateDTO.getName(), "Certificate 2"));
  }

  @Test
  void findByIdSuccessTest() {
    given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
    given(certificateRepository.findById(1L)).willReturn(Optional.of(certificate));
    CertificateDTO certificateDTOById = certificateService.getById(1L);
    assertThat(certificateDTOById).isNotNull();
  }

  @Test
  void getByTagsNameSuccessTest() {
    given(certificateMapper.toCertificateDTO(any(Certificate.class))).willReturn(certificateDTO);
    List<String> namesList = Arrays.asList("tag1", "tag2");
    List<Certificate> certificateList = CertificateTestData.buildCertificates();
    given(certificateRepository.findByTagsNameIgnoreCaseIsIn(namesList, pageable)).willReturn(certificateList);
    Set<CertificateDTO> byTagsName = certificateService.getByTagsName(namesList, pageable);
    assertThat(byTagsName.size()).isEqualTo(1);
  }

  @Test
  void findAllSuccessTest() {
    List<Certificate> certificateList = CertificateTestData.buildCertificates();
    given(certificateRepository.findAll(pageable)).willReturn(new PageImpl<>(certificateList));
    List<CertificateDTO> certificateDTOS = certificateService.getAll(pageable);
    assertAll(() -> assertThat(certificateDTOS).isNotNull(),
        () -> assertThat(certificateDTOS.size()).isEqualTo(2));
  }

  @Test
  void findByTagOrDescriptionSuccessTest() {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
    Example<Certificate> certificateExample = Example.of(CertificateTestData.buildCertificateForExample(),
        exampleMatcher);
    certificate.getTags().add(TagTestData.buildTagOne());
    certificateDTO.getTags().add(TagTestData.buildTagDTO());
    List<Certificate> certificateList = CertificateTestData.buildCertificates();
    Page<Certificate> page = new PageImpl<>(certificateList);
    given(certificateRepository.findAll(certificateExample, pageable,
        NamedEntityGraph.loading(Constants.ENTITY_GRAPH_NAME_CERTIFICATE_TAG))).willReturn(page);
    List<CertificateDTO> byTagOrDescription = certificateService
        .getByNameDescription(pageable, certificate.getName(), "The best");
    assertThat(byTagOrDescription).isNotNull();
  }

  @Test
  void deleteSuccessTest() {
    given(certificateRepository.findById(any())).willReturn(Optional.of(certificate));
    certificateService.delete(1L);
    verify(certificateRepository, times(1)).delete(Mockito.any(Certificate.class));
  }

  @Test
  void findByNameSuccessTest() {
    given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
    given(certificateRepository.findByName(Mockito.any(String.class))).willReturn(Optional.of(certificate));
    CertificateDTO byName = certificateService.getByName(certificate.getName());
    assertAll(() -> assertThat(byName).isNotNull(),
        () -> assertEquals(byName.getName(), certificate.getName()));
  }
}