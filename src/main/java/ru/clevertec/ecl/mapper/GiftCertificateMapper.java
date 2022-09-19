package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.entity.GiftCertificate;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {

    GiftCertificateDTO toGiftCertificateDTO(GiftCertificate giftCertificate);
    GiftCertificate toGiftCertificate(GiftCertificateDTO giftCertificate);
}
