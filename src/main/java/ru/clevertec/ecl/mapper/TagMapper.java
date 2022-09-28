package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.annatation.CertificateWithoutTags;
import ru.clevertec.ecl.annatation.TagWithoutCertificates;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = CertificateMapper.class)
public interface TagMapper {

    @Mapping(target = "certificates", qualifiedBy = CertificateWithoutTags.class)
    TagDTO toTagDTO(Tag tag);

    @TagWithoutCertificates
    @Mapping(target = "certificates", ignore = true)
    TagDTO toTagDTOWithoutCertificate(Tag tag);

    Tag toTag(TagDTO tagDTO);

    Set<Tag> toTagSet(Set<TagDTO> tagDTOSets);

    List<TagDTO> toTagDTOList(List<Tag> tagSets);

}
