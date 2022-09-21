package ru.clevertec.ecl.mapper;

import org.mapstruct.*;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.annatation.CertificateWithoutTags;
import ru.clevertec.ecl.annatation.TagWithoutCertificates;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TagMapper.class)
public interface CertificateMapper {


    @Mapping(target = "tags", qualifiedBy = TagWithoutCertificates.class)
    CertificateDTO toGiftCertificateDTO(Certificate certificate);

    @CertificateWithoutTags
    @Mapping(target = "tags", ignore = true)
    CertificateDTO toGiftCertificateDTOWithoutTag(Certificate certificate);


    Certificate toGiftCertificate(CertificateDTO giftCertificate);
}
