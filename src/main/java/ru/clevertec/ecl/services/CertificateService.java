package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.CertificateDTO;

import java.util.List;
import java.util.Set;

public interface CertificateService {

    CertificateDTO save(CertificateDTO certificateDTO);

    CertificateDTO update(Long id, CertificateDTO certificateDTO);

    CertificateDTO getById(Long id);

    List<CertificateDTO> getAll(Pageable pageable);

    List<CertificateDTO> getByNameDescription(Pageable pageable, String tagName, String description);

    void delete(Long id);

    CertificateDTO getByName(String name);

    Set<CertificateDTO> getByTagsName(List<String> tagsNames, Pageable pageable);
}
