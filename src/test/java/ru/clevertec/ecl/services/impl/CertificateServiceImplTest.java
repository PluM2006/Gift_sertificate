package ru.clevertec.ecl.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dmain.dto.CertificateDTO;
import ru.clevertec.ecl.dmain.dto.TagDTO;
import ru.clevertec.ecl.dmain.entity.Certificate;
import ru.clevertec.ecl.dmain.entity.Tag;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.repository.CertificateRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    private CertificateDTO certificateDTO;
    private Certificate certificate;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        certificate = getCertificate();
        certificateDTO = getCertificateDTO();
        pageable = PageRequest.of(1, 2, Sort.by("name, desc"));
    }

    @Test
    void save() {
        given(certificateRepository.existsByName(any())).willReturn(false);
        given(certificateRepository.save(any())).willReturn(certificate);
        given(certificateMapper.toCertificateDTO(any())).willReturn(certificateDTO);
        given(certificateMapper.toCertificate(any())).willReturn(certificate);
        CertificateDTO saveCertificateDTO = certificateService.save(certificateDTO);
        Assertions.assertThat(saveCertificateDTO).isNotNull();
//        Assertions.assertThat(saveCertificateDTO.getLastUpdateDate()).isNotNull();
//        Assertions.assertThat(saveCertificateDTO.getCreateDate()).isNotNull();
    }

    @Test
    void update() {
        given(certificateRepository.save(any())).willReturn(certificate);
        given(certificateRepository.findById(any())).willReturn(Optional.of(certificate));
        given(certificateMapper.toCertificateDTO(any())).willReturn(certificateDTO);
        certificateDTO.setName("Certificate 2");
        CertificateDTO update = certificateService.update(1L, certificateDTO);
        System.out.println(update);
        Assertions.assertThat(update).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(certificateDTO.getName(), "Certificate 2");
    }

    @Test
    void findById() {
        given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
        given(certificateRepository.findById(1L)).willReturn(Optional.of(certificate));
        CertificateDTO certificateDTOById = certificateService.findById(1L);
        Assertions.assertThat(certificateDTOById).isNotNull();
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
                .tags(new HashSet<>())
                .build());
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        certificateList.add(Certificate.builder()
                .id(1L)
                .name("Certificate 2")
                .description("The best certificate 2")
                .price(BigDecimal.valueOf(20))
                .tags(new HashSet<>())
                .build());
        given(certificateRepository.findAll(pageable)).willReturn(new PageImpl<>(certificateList));
        given(certificateMapper.toCertificateDTOList(Mockito.anyList())).willReturn(certificateDTOList);
        List<CertificateDTO> certificateDTOS = certificateService.findAll(pageable);
        Assertions.assertThat(certificateDTOS).isNotNull();
        Assertions.assertThat(certificateDTOS.size()).isEqualTo(2);
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
        given(certificateRepository.findByTagNameDescription(any(), any(), any())).willReturn(certificateList);
        List<CertificateDTO> byTagOrDescription = certificateService.findByTagOrDescription(pageable, tagDTO.getName(), "The best", new String[]{"name, desc"});
        Assertions.assertThat(byTagOrDescription).isNotNull();
    }

    @Test
    void findByTagOrDescriptionOrderNegative() {
        TagDTO tagDTO = TagDTO.builder()
                .id(1L)
                .name("New Tag").build();
        org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
                () -> certificateService.findByTagOrDescription(pageable, tagDTO.getName(), "The best", new String[]{"name "}));
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
        CertificateDTO byName = certificateService.findByName("Certificate 1");
        Assertions.assertThat(byName).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(byName.getName(), "Certificate 1");
    }

    private Certificate getCertificate() {
        return Certificate.builder()
                .id(1L)
                .name("Certificate 1")
                .description("The best certificate")
                .price(BigDecimal.valueOf(20))
                .tags(new HashSet<>())
                .build();
    }

    private CertificateDTO getCertificateDTO() {
        return CertificateDTO.builder()
                .id(1L)
                .name("Certificate 1")
                .description("The best certificate")
                .price(BigDecimal.valueOf(20))
                .tags(new HashSet<>())
                .build();
    }
}