package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.TagDTO;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;

public interface TagService {

    TagDTO save(TagDTO tagDTO);

    Set<TagDTO> saveAll(Set<TagDTO> tagDTOSet);

    TagDTO update(TagDTO tagDTO);

    TagDTO findById(Long id);

    List<TagDTO> findAll(Pageable pageable);

    boolean delete(Long id);

}
