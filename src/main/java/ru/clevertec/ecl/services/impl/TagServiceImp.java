package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.services.TagService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImp implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagDTO getPupularTagUser(String username){
        return tagRepository.findPopularTag(username)
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new NotFoundException("Username", "username", username));
    }

    @Override
    public TagDTO getById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new NotFoundException("Tag", "id", id));
    }

    @Override
    public List<TagDTO> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable).stream().map(tagMapper::toTagDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TagDTO save(TagDTO tagDTO) {
        return tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(tagDTO)));
    }

    @Transactional
    @Override
    public List<TagDTO> saveAll(List<TagDTO> tagDTOList) {
        return tagDTOList.stream().map(tagMapper::toTag)
                .map(tag -> tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag)))
                .map(tagMapper::toTagDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TagDTO update(Long id, TagDTO tagDTO) {
        return tagMapper.toTagDTO(tagRepository
                .findById(id)
                .map(tag -> tagRepository.save(tagMapper.toTag(tagDTO)))
                .orElseThrow(() -> new NotFoundException("Tag", "id", id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        tagRepository.findById(id)
                .map(tag -> {
                    tagRepository.delete(tag);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Tag", "id", id));
    }

}
