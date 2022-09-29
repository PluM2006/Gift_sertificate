package ru.clevertec.ecl.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceImpTest {
    @InjectMocks
    private TagServiceImp tagService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagMapper tagMapper;
    private TagDTO tagDTO;
    private Tag tag;

    @BeforeEach
    void setUp() {
        tagDTO = getTagDTO();
        tag = getTag();
    }

    @Test
    void saveTest() {
        given(tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(Mockito.any(TagDTO.class)))))
                .willReturn(tagDTO);
        TagDTO save = tagService.save(tagDTO);
        assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("ResponseStatusException test Save")
    void saveNegativeTest() {
        given(tagRepository.existsByName(tagDTO.getName())).willReturn(true);
        assertThrows(
                ResponseStatusException.class,
                () -> tagService.save(tagDTO)
        );
    }

    @Test
    void saveAllWhenSetNotNull() {
        given(tagRepository.findByName(any())).willReturn(Optional.of(tag));
        given(tagMapper.toTagDTO(any())).willReturn(tagDTO);
        List<TagDTO> tagDTOSet = new ArrayList<>();
        tagDTOSet.add(tagDTO);
        List<TagDTO> tagDTOs = tagService.saveAll(tagDTOSet);
        assertThat(tagDTOs).isNotNull();
        assertThat(tagDTOs.size()).isEqualTo(1);
    }

    @Test
    void update() {
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
        given(tagRepository.save(tagMapper.toTag(Mockito.any(TagDTO.class)))).willReturn(tag);
        given(tagMapper.toTagDTO(Mockito.any(Tag.class))).willReturn(tagDTO);
        tagDTO.setName("new Tag");
        TagDTO updateTag = tagService.update(1L, tagDTO);
        assertThat(updateTag.getName()).isEqualTo("new Tag");
    }

    @Test
    void getById() {
        given(tagMapper.toTagDTO(tag)).willReturn(tagDTO);
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
        TagDTO byId = tagService.getById(1L);
        assertThat(byId).isNotNull();
    }

    @Test
    void getAllTags() {
        Pageable pageable = Pageable.ofSize(1);
        List<TagDTO> tagListDTO = new ArrayList<>();
        tagListDTO.add(tagDTO);
        tagListDTO.add(TagDTO.builder()
                .id(2L)
                .name("Tag2").build());
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        tagList.add(Tag.builder()
                .id(2L)
                .name("Tag2").build());
        given(tagRepository.findAll(pageable)).willReturn(new PageImpl<>(tagList));
        given(tagMapper.toTagDTOList(Mockito.anyList())).willReturn(tagListDTO);
        List<TagDTO> allTags = tagService.getAllTags(pageable);
        assertThat(allTags).isNotNull();
        assertThat(allTags.size()).isEqualTo(2);
    }

    @Test
    void delete() {
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
        tagService.delete(1L);
        verify(tagRepository, times(1)).delete(Mockito.any(Tag.class));
    }

    private TagDTO getTagDTO() {
        return TagDTO.builder()
                .id(1L)
                .name("tag")
                .build();
    }

    private Tag getTag() {
        return Tag.builder()
                .id(1L)
                .name("tag")
                .build();
    }
}
