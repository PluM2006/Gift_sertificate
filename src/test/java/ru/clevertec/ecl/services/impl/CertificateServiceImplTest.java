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
import ru.clevertec.ecl.dmain.dto.CertificateDTO;
import ru.clevertec.ecl.dmain.dto.TagDTO;
import ru.clevertec.ecl.dmain.entity.Certificate;
import ru.clevertec.ecl.dmain.entity.Tag;
import ru.clevertec.ecl.exctention.CertificateDtoResolver;
import ru.clevertec.ecl.exctention.CertificateResolver;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
//@ExtendWith(CertificateResolver.class)
//@ExtendWith(CertificateDtoResolver.class)
class CertificateServiceImplTest {
    @InjectMocks
    private CertificateServiceImpl certificateService;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private TagServiceImp tagService;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private CertificateMapper certificateMapper;
    private CertificateDTO certificateDTO;
    private Certificate certificate;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        certificateDTO = CertificateDTO.builder()
                .id(1L)
                .name("Certificate 1")
                .description("The best certificate")
                .price(BigDecimal.valueOf(20))
                .tags(new HashSet<>())
                .build();
        certificate = Certificate.builder()
                .id(1L)
                .name("Certificate 1")
                .description("The best certificate")
                .price(BigDecimal.valueOf(20))
                .tags(new HashSet<>())
                .build();
        pageable = PageRequest.of(1, 2, Sort.by("name, desc"));
    }

    @Test
    void save() {
        given(certificateMapper.toCertificate(Mockito.any(CertificateDTO.class))).willReturn(certificate);
        given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
        given(certificateRepository.save(certificate)).willReturn(certificate);
        Certificate save = certificateRepository.save(certificate);
        System.out.println(save);

//        given(tagMapper.toTagSet(Mockito.anySet())).willReturn(Mockito.anySet());
//        given(tagService.saveAll(certificateDTO.getTags())).willReturn(Mockito.anySet());
        CertificateDTO saveCertificateDTO = certificateService.save(certificateDTO);
        System.out.println(saveCertificateDTO);
        Assertions.assertThat(saveCertificateDTO).isNotNull();
        Assertions.assertThat(saveCertificateDTO.getLastUpdateDate()).isNotNull();
        Assertions.assertThat(saveCertificateDTO.getCreateDate()).isNotNull();
    }

    @Test
    void update() {
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
        given(certificateRepository.findByTagNameDescription("New Tag", "The best", pageable)).willReturn(certificateList);
        List<CertificateDTO> byTagOrDescription = certificateService.findByTagOrDescription(pageable, tagDTO.getName(), "The best", new String[]{"name, desc"});
        Assertions.assertThat(byTagOrDescription).isNotNull();
    }

    @Test
    void delete() {
    }

    @Test
    void findByName() {
        given(certificateMapper.toCertificateDTO(Mockito.any(Certificate.class))).willReturn(certificateDTO);
        given(certificateRepository.findByName(Mockito.any(String.class))).willReturn(Optional.of(certificate));
        CertificateDTO byName = certificateService.findByName("Certificate 1");
        Assertions.assertThat(byName).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(byName.getName(), "Certificate 1");
    }
}