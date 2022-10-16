package ru.clevertec.ecl.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.TagFactory;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

  @InjectMocks
  private TagServiceImpl tagService;

  @Mock
  private TagRepository tagRepository;

  @Mock
  private TagMapper tagMapper;
  private TagDTO tagDTO;
  private Tag tag;

  @BeforeEach
  void setUp() {
    tagDTO = TagFactory.buildTagDTO();
    tag = TagFactory.buildTagOne();
  }

  @Test
  void saveSuccessTest() {
    given(tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(Mockito.any(TagDTO.class))))).willReturn(tagDTO);
    TagDTO save = tagService.save(tagDTO);
    assertThat(save).isNotNull();
  }

  @Test
  void saveAllSuccessTest() {
    given(tagRepository.findByName(any())).willReturn(Optional.of(tag));
    given(tagMapper.toTagDTO(any())).willReturn(tagDTO);
    given(tagMapper.toTag(any())).willReturn(tag);
    List<TagDTO> tagDTOSet = new ArrayList<>();
    tagDTOSet.add(tagDTO);
    List<TagDTO> tagDTOs = tagService.saveAll(tagDTOSet);
    assertAll(() -> assertThat(tagDTOs).isNotNull(),
        () -> assertThat(tagDTOs.size()).isEqualTo(1));
  }

  @Test
  void updateSuccessTest() {
    given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
    given(tagRepository.save(tagMapper.toTag(Mockito.any(TagDTO.class)))).willReturn(tag);
    given(tagMapper.toTagDTO(Mockito.any(Tag.class))).willReturn(tagDTO);
    tagDTO.setName("new Tag");
    TagDTO updateTag = tagService.update(1L, tagDTO);
    assertThat(updateTag.getName()).isEqualTo("new Tag");
  }

  @Test
  void getByIdSuccessTest() {
    given(tagMapper.toTagDTO(tag)).willReturn(tagDTO);
    given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
    TagDTO byId = tagService.getById(1L);
    assertThat(byId).isNotNull();
  }

  @Test
  void getPopularTagUserTestSuccessTest() {
    given(tagMapper.toTagDTO(tag)).willReturn(tagDTO);
    given(tagRepository.findPopularTagUser()).willReturn(Optional.of(tag));
    TagDTO popularTagUser = tagService.getPopularTagUser();
    assertThat(popularTagUser).isNotNull();
  }

  @Test
  void getAllTags() {
    Pageable pageable = Pageable.ofSize(1);
    List<Tag> tagList = new ArrayList<>();
    tagList.add(tag);
    tagList.add(TagFactory.buildTagTwo());
    given(tagRepository.findAll(pageable)).willReturn(new PageImpl<>(tagList));
    List<TagDTO> allTags = tagService.getAllTags(pageable);
    assertAll(() -> assertThat(allTags).isNotNull(),
        () -> assertThat(allTags.size()).isEqualTo(2));
  }

  @Test
  void delete() {
    given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
    tagService.delete(1L);
    verify(tagRepository, times(1)).delete(Mockito.any(Tag.class));
  }
}
