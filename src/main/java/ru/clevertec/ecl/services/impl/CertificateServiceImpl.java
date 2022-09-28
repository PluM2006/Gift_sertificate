package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.TagService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @Transactional
    @Override
    public CertificateDTO save(CertificateDTO certificateDTO) {
        Certificate certificate = certificateMapper.toCertificate(certificateDTO);
        certificate.setTags(tagMapper.toTagList(tagService.saveAll(tagMapper.toTagDTOList(certificate.getTags()))));
        return certificateMapper.toCertificateDTO(certificateRepository.save(certificate));
    }

    @Transactional
    @Override
    public CertificateDTO update(Long id, CertificateDTO certificateDTO) {
        return certificateMapper.toCertificateDTO(certificateRepository
                .findById(id)
                .map(certificate -> certificateRepository.save(certificationToUpdate(certificateDTO, certificate)))
                .orElseThrow(() -> new NotFoundException("Certificate", "id", id)));
    }

    @Override
    public CertificateDTO findById(Long id) {
        return certificateRepository.findById(id)
                .map(certificateMapper::toCertificateDTO)
                .orElseThrow(() -> new NotFoundException("Certificate", "id", id));
    }

    @Override
    public List<CertificateDTO> findAll(Pageable pageable) {
        return certificateMapper.toCertificateDTOList(certificateRepository
                .findAll(pageable).getContent());
    }

    @Override
    public List<CertificateDTO> findByTagOrDescription(Pageable pageable, String tagName, String description) {
        return certificateMapper
                .toCertificateDTOList(certificateRepository.findByTagNameDescription(
                        tagName,
                        description,
                        pageable));
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return certificateRepository.findById(id)
                .map(certificate -> {
                    certificateRepository.delete(certificate);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Certificate", "id", id));
    }

    @Override
    public CertificateDTO findByName(String name) {
        return certificateRepository.findByName(name)
                .map(certificateMapper::toCertificateDTO)
                .orElseThrow(() -> new NotFoundException("Certificate", "name", name));
    }

    private Certificate certificationToUpdate(CertificateDTO certificateDTO, Certificate certificate) {
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setName(certificateDTO.getName());
        certificate.setDuration(certificateDTO.getDuration());
        certificate.setPrice(certificateDTO.getPrice());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setTags(tagMapper.toTagList(tagService.saveAll(certificateDTO.getTags())));
        return certificate;
    }
}
