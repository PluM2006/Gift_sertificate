package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dmain.dto.TagDTO;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.services.TagService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImp implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional
    @Override
    public TagDTO save(TagDTO tagDTO) {
        if (!tagRepository.existsByName(tagDTO.getName())) {
            return tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(tagDTO)));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name exists = " + tagDTO.getName()); // TODO: 23.09.22
        }
    }

    @Transactional
    @Override
    public Set<TagDTO> saveAll(Set<TagDTO> tagDTOSet) {
        Set<TagDTO> result = new HashSet<>();
        for (TagDTO tagDTO : tagDTOSet) {
            result.add(tagRepository.findByName(tagDTO.getName())
                    .map(tagMapper::toTagDTO)
                    .orElseGet(() -> tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(tagDTO)))));
        }
        return result;
    }

    @Transactional
    @Override
    public TagDTO update(Long id, TagDTO tagDTO) {
        // TODO: 23.09.22 отлавить duplicate
        return tagMapper.toTagDTO(tagRepository
                .findById(id)
                .map(tag -> tagRepository.save(tagMapper.toTag(tagDTO)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id=" + id)));
    }

    @Override
    public TagDTO getById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<TagDTO> getAllTags(Pageable pageable) {
        return tagMapper.toTagDTOList(
                tagRepository.findAll(pageable).getContent());
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return tagRepository.findById(id)
                .map(tag -> {
                    tagRepository.delete(tag);
                    return true;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
