package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.services.TagService;

import java.awt.print.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagCertificateServiceImp implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public void saveAll(Set<TagDTO> tags) {
        checkNullTags(tags).forEach(tag -> tag.setId(tagRepository.save(tagMapper.toTag(tag)).getId()));
    }

    @Override
    public TagDTO save(TagDTO tagDTO) {
        return null;
    }

    @Override
    public TagDTO update(TagDTO tagDTO) {
        return null;
    }

    @Override
    public TagDTO findById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<TagDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        tagRepository.findById(id)
                .map(tag -> {
                            tagRepository.delete(tag);
                            return true;
                        }
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ;
        return false;
    }

    private Set<TagDTO> checkNullTags(Set<TagDTO> tagList) {
        return (tagList == null) ? new HashSet<>() : tagList;
    }

}
