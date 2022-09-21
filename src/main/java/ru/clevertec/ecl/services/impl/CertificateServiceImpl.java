package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.CycleAvoidingMappingContext;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.CertificateService;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagCertificateServiceImp tagCertificateServiceImp;
    private final CertificateMapper certificateMapper;

    @Transactional
    @Override
    public CertificateDTO save(CertificateDTO certificateDTO) {
        Certificate certificate = certificateMapper.toGiftCertificate(certificateDTO);
        tagCertificateServiceImp.saveTags(certificate.getTags());
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(certificate.getCreateDate());
        return certificateMapper.toGiftCertificateDTO(certificateRepository.save(certificate));
    }

    @Transactional
    @Override
    public CertificateDTO update(CertificateDTO certificateDTO) {
        Certificate certificate = certificateMapper.toGiftCertificate(certificateDTO);
        tagCertificateServiceImp.saveTags(certificate.getTags());
        certificate.setLastUpdateDate(LocalDateTime.now());
        return certificateMapper.toGiftCertificateDTO(certificateRepository.save(certificate));
    }

    @Override
    public CertificateDTO findById(Long id) {
        return certificateRepository.findById(id)
                .map((Certificate certificate) -> certificateMapper.toGiftCertificateDTO(certificate))
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
}
