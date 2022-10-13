package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(imports = {LocalDateTime.class, BigDecimal.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
public interface CertificateMapper {

    CertificateDTO toCertificateDTO(Certificate certificate);

    @Mapping(target = "createDate", source = "createDate", defaultExpression = "java(LocalDateTime.now())")
    @Mapping(target = "lastUpdateDate", source = "lastUpdateDate", defaultExpression = "java(LocalDateTime.now())")
    Certificate toCertificate(CertificateDTO giftCertificate);

    List<CertificateDTO> toCertificateDTOList(List<Certificate> certificates);

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "price", source = "price", defaultExpression = "java(BigDecimal.ZERO)")
    @Mapping(target = "lastUpdateDate", defaultExpression = "java(LocalDateTime.now())")
    Certificate certificateToUpdate(CertificateDTO certificateDTO, @MappingTarget Certificate certificate);
}
