package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.CertificateDTO;

import java.util.List;

public interface CertificateService {

    CertificateDTO save(CertificateDTO certificateDTO);

    CertificateDTO update(Long id, CertificateDTO certificateDTO);

    CertificateDTO getById(Long id);

    List<CertificateDTO> getAll(Pageable pageable);

    List<CertificateDTO> getByTagOrDescription(Pageable pageable, String tagName, String description);

    void delete(Long id);

    CertificateDTO getByName(String name);
}
