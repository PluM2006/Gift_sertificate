package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toTagDTO(Tag tag);

    Tag toTag(TagDTO tagDTO);

    List<Tag> toTagList(List<TagDTO> tagDTOSets);

    List<TagDTO> toTagDTOList(List<Tag> tagSets);

}
