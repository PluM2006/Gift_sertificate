package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dmain.dto.TagDTO;

import java.util.List;
import java.util.Set;

public interface TagService {

    TagDTO save(TagDTO tagDTO);

    Set<TagDTO> saveAll(Set<TagDTO> tagDTOSet);

    TagDTO update(Long id, TagDTO tagDTO);

    TagDTO getById(Long id);

    List<TagDTO> getAllTags(Pageable pageable);

    boolean delete(Long id);

}
