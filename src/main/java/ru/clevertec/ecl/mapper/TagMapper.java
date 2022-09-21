package ru.clevertec.ecl.mapper;

import org.mapstruct.*;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.annatation.CertificateWithoutTags;
import ru.clevertec.ecl.annatation.TagWithoutCertificates;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = CertificateMapper.class)
public interface TagMapper {

    @Mapping(target = "certificates", qualifiedBy = CertificateWithoutTags.class)
    TagDTO toTagDTO(Tag tag);

    @TagWithoutCertificates
    @Mapping(target = "certificates", ignore = true)
    TagDTO toTagDTOWithoutCertificate(Tag tag);

    Tag toTag (TagDTO tagDTO);
}
