package ru.clevertec.ecl.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;

@Mapper(imports = {LocalDateTime.class, BigDecimal.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
public interface CertificateMapper {

  CertificateDTO toCertificateDTO(Certificate certificate);

  @Mapping(target = "createDate",expression = "java(LocalDateTime.now())")
  @Mapping(target = "lastUpdateDate", source = "lastUpdateDate", defaultExpression = "java(LocalDateTime.now())")
  Certificate toCertificate(CertificateDTO giftCertificate);

  @Mapping(target = "createDate", ignore = true)
  @Mapping(target = "price", source = "price", defaultExpression = "java(BigDecimal.ZERO)")
  @Mapping(target = "lastUpdateDate", defaultExpression = "java(LocalDateTime.now())")
  Certificate certificateToUpdate(CertificateDTO certificateDTO, @MappingTarget Certificate certificate);
}
