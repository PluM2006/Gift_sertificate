package ru.clevertec.ecl.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.data.CertificateFactory;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.TagService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        given(certificateRepository.save(certificate)).willReturn(certificate);
        given(certificateRepository.findById(any())).willReturn(Optional.of(certificate));
        given(certificateMapper.toCertificateDTO(certificate)).willReturn(certificateDTO);
        given(tagService.saveAll(certificateDTO.getTags())).willReturn(new ArrayList<>());
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
    void findAll() {
        List<CertificateDTO> certificateDTOList = new ArrayList<>();
        certificateDTOList.add(certificateDTO);
        certificateDTOList.add(CertificateDTO.builder()
                .id(1L)
                .name("Certificate 2")
                .description("The best certificate 2")
                .price(BigDecimal.valueOf(20))
                .tags(new ArrayList<>())
                .build());
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
        Tag tag = Tag.builder()
                .id(1L)
                .name("New Tag").build();
        TagDTO tagDTO = TagDTO.builder()
                .id(1L)
                .name("New Tag").build();
        certificate.getTags().add(tag);
        certificateDTO.getTags().add(tagDTO);
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        List<CertificateDTO> certificateDTOList = new ArrayList<>();
        certificateDTOList.add(certificateDTO);
        given(certificateMapper.toCertificateDTOList(Mockito.anyList())).willReturn(certificateDTOList);
        given(certificateRepository.findAll(Example.of(certificate))).willReturn(certificateList);
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
        CertificateDTO byName = certificateService.getByName("Certificate 1");
        assertAll(() -> assertThat(byName).isNotNull(),
                () -> assertEquals(byName.getName(), "Certificate 1"));
    }
}