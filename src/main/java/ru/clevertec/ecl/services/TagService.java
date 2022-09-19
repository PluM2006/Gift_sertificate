package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.TagDTO;

import java.awt.print.Pageable;
import java.util.List;

public interface TagService {

    TagDTO save(TagDTO tagDTO);

    TagDTO update(TagDTO tagDTO);

    TagDTO findById(String id);

    List<TagDTO> findAll(Pageable pageable);

    boolean delete(Long id);
}
