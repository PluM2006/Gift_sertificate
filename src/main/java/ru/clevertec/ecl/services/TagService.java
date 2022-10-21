package ru.clevertec.ecl.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.TagDTO;

public interface TagService {

  TagDTO save(TagDTO tagDTO);

  TagDTO getPopularTagUser();

  List<TagDTO> saveAll(List<TagDTO> tagDTOList);

  TagDTO update(Long id, TagDTO tagDTO);

  TagDTO getById(Long id);

  List<TagDTO> getAllTags(Pageable pageable);

  void delete(Long id);
}
