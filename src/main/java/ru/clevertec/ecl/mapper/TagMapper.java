package ru.clevertec.ecl.mapper;

import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toTagDTO(Tag tag, @Context CycleAvoidingMappingContext context);
      Tag toTag (TagDTO tagDTO, @Context CycleAvoidingMappingContext context);
}
