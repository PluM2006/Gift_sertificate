package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.TagService;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @Transactional
    @Override
    public CertificateDTO save(CertificateDTO certificateDTO) {
        Certificate certificate = certificateMapper.toGiftCertificate(certificateDTO);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setTags(tagMapper.toTagSet(tagService.saveAll(certificateDTO.getTags())));
        return certificateMapper.toGiftCertificateDTO(certificateRepository.save(certificate));
    }

    @Transactional
    @Override
    public CertificateDTO update(Long id, CertificateDTO certificateDTO) {
        return certificateMapper.toGiftCertificateDTO(certificateRepository
                .findById(id)
                .map(certificate -> certificateRepository.save(CertificatedtoToUpdate(certificateDTO, certificate)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Override
    public CertificateDTO findById(Long id) {
        return certificateRepository.findById(id)
                .map(certificateMapper::toGiftCertificateDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<CertificateDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        certificateRepository.findById(id)
                .map(certificate ->
                {
                    certificateRepository.delete(certificate);
                    return true;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return false;
    }

    private Certificate CertificatedtoToUpdate(CertificateDTO certificateDTO, Certificate certificate) {
        certificate.setName(certificateDTO.getName());
        certificate.setDuration(certificateDTO.getDuration());
        certificate.setPrice(certificateDTO.getPrice());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setTags(tagMapper.toTagSet(tagService.saveAll(certificateDTO.getTags())));
        return certificate;
    }

}
