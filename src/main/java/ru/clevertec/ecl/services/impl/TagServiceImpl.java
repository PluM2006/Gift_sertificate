package ru.clevertec.ecl.services.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.entity.commitLog.Operation;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.services.commitLog.CommitLogService;
import ru.clevertec.ecl.services.TagService;
import ru.clevertec.ecl.utils.Constants;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;
  private final CommitLogService commitLogService;

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
    TagDTO saveTag = tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(tagDTO)));
    log.info("CommitLog: save Tag");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.SAVE, saveTag, Constants.TAG);
    log.info("Result commitLog: save Tag {}", commitLogService.write(commitLog));
    return saveTag;
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
    TagDTO updateTag = tagMapper.toTagDTO(tagRepository.findById(id)
        .map(tag -> tagRepository.save(tagMapper.toTag(tagDTO)))
        .orElseThrow(() -> new EntityNotFoundException(Constants.TAG, Constants.FIELD_NAME_ID, id)));
    log.info("CommitLog: update Tag");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.UPDATE, updateTag, Constants.TAG);
    log.info("Result commitLog: update Tag{}", commitLogService.write(commitLog));
    return updateTag;
  }

  @Transactional
  @Override
  public void delete(Long id) {
    Tag deleteTag = tagRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.TAG, Constants.FIELD_NAME_ID, id));
    tagRepository.delete(deleteTag);
    log.info("CommitLog: delete Tag");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.DELETE,
        tagMapper.toTagDTO(deleteTag), Constants.TAG);
    log.info("Result commitLog: delete Tag {}", commitLogService.write(commitLog));
  }
}
