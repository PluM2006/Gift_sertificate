package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(imports = {LocalDateTime.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
public interface CertificateMapper {

    CertificateDTO toCertificateDTO(Certificate certificate);

    @Mapping(target = "createDate", source = "createDate", defaultExpression = "java(LocalDateTime.now())")
    @Mapping(target = "lastUpdateDate", source = "lastUpdateDate", defaultExpression = "java(LocalDateTime.now())")
    Certificate toCertificate(CertificateDTO giftCertificate);

    List<CertificateDTO> toCertificateDTOList(List<Certificate> certificates);
}
