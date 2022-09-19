package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    CertificateDTO toGiftCertificateDTO(Certificate certificate);
    Certificate toGiftCertificate(CertificateDTO giftCertificate);
}
