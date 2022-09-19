package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.GiftCertificateDTO;

import java.awt.print.Pageable;
import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDTO save(GiftCertificateDTO giftCertificateDTO);

    GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO);

    GiftCertificateDTO findById(String id);

    List<GiftCertificateDTO> findAll(Pageable pageable);

    void delete(String id);
}
