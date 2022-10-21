package ru.clevertec.ecl.services.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.services.TagService;
import ru.clevertec.ecl.utils.Constants;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public TagDTO getPopularTagUser() {
    return tagRepository.findPopularTagUser()
        .map(tagMapper::toTagDTO)
        .orElseThrow(RuntimeException::new);
  }

  @Override
  public TagDTO getById(Long id) {
    return tagRepository.findById(id)
        .map(tagMapper::toTagDTO)
        .orElseThrow(() -> new EntityNotFoundException(Constants.TAG, Constants.FIELD_NAME_ID, id));
  }

  @Override
  public List<TagDTO> getAllTags(Pageable pageable) {
    return tagRepository.findAll(pageable).stream()
        .map(tagMapper::toTagDTO)
        .collect(toList());
  }

  @Transactional
  @Override
  public TagDTO save(TagDTO tagDTO) {
    return tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(tagDTO)));
  }

  @Transactional
  @Override
  public List<TagDTO> saveAll(List<TagDTO> tagDTOList) {
    return tagDTOList.stream()
        .map(tagMapper::toTag)
        .map(tag -> tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag)))
        .map(tagMapper::toTagDTO)
        .collect(toList());
  }

  @Transactional
  @Override
  public TagDTO update(Long id, TagDTO tagDTO) {
    return tagMapper.toTagDTO(tagRepository.findById(id)
        .map(tag -> tagRepository.save(tagMapper.toTag(tagDTO)))
        .orElseThrow(() -> new EntityNotFoundException(Constants.TAG, Constants.FIELD_NAME_ID, id)));
  }

  @Transactional
  @Override
  public void delete(Long id) {
    tagRepository.findById(id)
        .map(tag -> {
          tagRepository.delete(tag);
          return true;
        })
        .orElseThrow(() -> new EntityNotFoundException(Constants.TAG, Constants.FIELD_NAME_ID, id));
  }
}
