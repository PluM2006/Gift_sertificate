package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.services.GiftCertificateService;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateMapper giftCertificateMapper;


    @Transactional
    @Override
    public GiftCertificateDTO save(GiftCertificateDTO giftCertificateDTO) {

        GiftCertificate giftCertificate = giftCertificateMapper.toGiftCertificate(giftCertificateDTO);
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());
        giftCertificateRepository.save(giftCertificate);
        return giftCertificateMapper.toGiftCertificateDTO(giftCertificate);

    }

    @Override
    public GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO) {
        return null;
    }

    @Override
    public GiftCertificateDTO findById(String id) {
        return null;
    }

    @Override
    public List<GiftCertificateDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
