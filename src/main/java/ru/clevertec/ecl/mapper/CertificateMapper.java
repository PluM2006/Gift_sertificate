package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.annatation.CertificateWithoutTags;
import ru.clevertec.ecl.annatation.TagWithoutCertificates;
import ru.clevertec.ecl.dmain.dto.CertificateDTO;
import ru.clevertec.ecl.dmain.entity.Certificate;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TagMapper.class)
public interface CertificateMapper {

    @Mapping(target = "tags", qualifiedBy = TagWithoutCertificates.class)
    CertificateDTO toGiftCertificateDTO(Certificate certificate);

    @CertificateWithoutTags
    @Mapping(target = "tags", ignore = true)
    CertificateDTO toGiftCertificateDTOWithoutTag(Certificate certificate);

    Certificate toGiftCertificate(CertificateDTO giftCertificate);

    List<CertificateDTO> toCertificateDTOList(List<Certificate> certificates);

}
