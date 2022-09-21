package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.CertificateDTO;

import java.awt.print.Pageable;
import java.util.List;

public interface CertificateService {

    CertificateDTO save(CertificateDTO certificateDTO);

    CertificateDTO update(Long id, CertificateDTO certificateDTO);

    CertificateDTO findById(Long id);

    List<CertificateDTO> findAll(Pageable pageable);

    boolean delete(Long id);

}
