package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.TagDTO;

import java.util.List;

public interface TagService {

    TagDTO save(TagDTO tagDTO);

    List<TagDTO> saveAll(List<TagDTO> tagDTOList);

    TagDTO update(Long id, TagDTO tagDTO);

    TagDTO getById(Long id);

    List<TagDTO> getAllTags(Pageable pageable);

    void delete(Long id);
}
