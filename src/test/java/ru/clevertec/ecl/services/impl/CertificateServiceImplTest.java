package ru.clevertec.ecl.services.impl;

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
import ru.clevertec.ecl.data.CertificateFactory;
import ru.clevertec.ecl.data.TagFactory;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.TagService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl certificateService;
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
        certificate = CertificateFactory.certificate();
        certificateDTO = CertificateFactory.certificateDTO();
        pageable = PageRequest.of(1, 2, Sort.by("name, desc"));
    }

    @Test
    void save() {
        given(certificateRepository.save(any())).willReturn(certificate);
        given(certificateMapper.toCertificateDTO(any())).willReturn(certificateDTO);
        given(certificateMapper.toCertificate(any())).willReturn(certificate);
        CertificateDTO saveCertificateDTO = certificateService.save(certificateDTO);
        assertThat(saveCertificateDTO).isNotNull();
    }

    @Test
    void update() {
        given(certificateMapper.certificateToUpdate(any(), any(), any(), any())).willReturn(certificate);
        given(certificateRepository.save(certificate)).willReturn(certificate);
        given(certificateRepository.findById(any())).willReturn(Optional.of(certificate));
        given(certificateMapper.toCertificateDTO(certificate)).willReturn(certificateDTO);
        certificateDTO.setName("Certificate 2");
        CertificateDTO update = certificateService.update(1L, certificateDTO);
        assertAll(() -> assertThat(update).isNotNull(),
                () -> assertEquals(certificateDTO.getName(), "Certificate 2"));
    }

    @Test
    void findById() {
        given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
        given(certificateRepository.findById(1L)).willReturn(Optional.of(certificate));
        CertificateDTO certificateDTOById = certificateService.getById(1L);
        assertThat(certificateDTOById).isNotNull();
    }

    @Test
    void getByTagsName() {
        given(certificateMapper.toCertificateDTO(any(Certificate.class))).willReturn(certificateDTO);
        List<String> namesList = Arrays.asList("tag1", "tag2");
        List<Certificate> certificateList = Arrays.asList(certificate);
        given(certificateRepository.findByTagsNameIgnoreCaseIsIn(namesList, pageable)).willReturn(certificateList);
        Set<CertificateDTO> byTagsName = certificateService.getByTagsName(namesList, pageable);
        assertThat(byTagsName.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        certificateList.add(Certificate.builder()
                .id(1L)
                .name("Certificate 2")
                .description("The best certificate 2")
                .price(BigDecimal.valueOf(20))
                .tags(new ArrayList<>())
                .build());
        given(certificateRepository.findAll(pageable)).willReturn(new PageImpl<>(certificateList));
        List<CertificateDTO> certificateDTOS = certificateService.getAll(pageable);
        assertAll(() -> assertThat(certificateDTOS).isNotNull(),
                () -> assertThat(certificateDTOS.size()).isEqualTo(2));
    }

    @Test
    void findByTagOrDescription() {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Certificate> certificateExample = Example.of(CertificateFactory.certificateForExample(),
                exampleMatcher);
        certificate.getTags().add(TagFactory.tag());
        certificateDTO.getTags().add(TagFactory.tagDTO());
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        Page<Certificate> page = new PageImpl<>(certificateList);
        given(certificateRepository.findAll(certificateExample, pageable)).willReturn(page);
        List<CertificateDTO> byTagOrDescription = certificateService
                .getByNameDescription(pageable, certificate.getName(), "The best");
        assertThat(byTagOrDescription).isNotNull();
    }

    @Test
    void delete() {
        given(certificateRepository.findById(any())).willReturn(Optional.of(certificate));
        certificateService.delete(1L);
        verify(certificateRepository, times(1)).delete(Mockito.any(Certificate.class));
    }

    @Test
    void findByName() {
        given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
        given(certificateRepository.findByName(Mockito.any(String.class))).willReturn(Optional.of(certificate));
        CertificateDTO byName = certificateService.getByName(certificate.getName());
        assertAll(() -> assertThat(byName).isNotNull(),
                () -> assertEquals(byName.getName(), certificate.getName()));
    }
}