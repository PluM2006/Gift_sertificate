package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dmain.dto.CertificateDTO;

import java.util.List;

public interface CertificateService {

    CertificateDTO save(CertificateDTO certificateDTO);

    CertificateDTO update(Long id, CertificateDTO certificateDTO);

    CertificateDTO findById(Long id);

    List<CertificateDTO> findAll(Pageable pageable);

    List<CertificateDTO> findByTagOrDescription(String tagName, String description);

    List<CertificateDTO> findAllB(Pageable pageable);

    boolean delete(Long id);

}
