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
    CertificateDTO toCertificateDTO(Certificate certificate);

    @CertificateWithoutTags
    @Mapping(target = "tags", ignore = true)
    CertificateDTO toCertificateDTOWithoutTag(Certificate certificate);

    Certificate toCertificate(CertificateDTO giftCertificate);

    List<CertificateDTO> toCertificateDTOList(List<Certificate> certificates);

}
