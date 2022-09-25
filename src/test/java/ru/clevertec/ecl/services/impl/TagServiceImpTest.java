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
import ru.clevertec.ecl.dmain.dto.TagDTO;
import ru.clevertec.ecl.dmain.entity.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        tagDTO = TagDTO.builder()
                .id(1L)
                .name("tag")
                .build();
        tag = Tag.builder()
                .id(1L)
                .name("tag")
                .build();
    }

    @Test
    void saveTest() {
        given(tagMapper.toTagDTO(tagRepository.save(tagMapper.toTag(Mockito.any(TagDTO.class)))))
                .willReturn(tagDTO);
        TagDTO save = tagService.save(tagDTO);
        Assertions.assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("ResponseStatusException test Save")
    void saveNegativeTest() {
        given(tagRepository.existsByName(tagDTO.getName())).willReturn(true);
        org.junit.jupiter.api.Assertions.assertThrows(
                ResponseStatusException.class,
                () -> tagService.save(tagDTO)
        );
    }

    @Test
    void saveAll() {
    }

    @Test
    void update() {
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
        given(tagRepository.save(tagMapper.toTag(Mockito.any(TagDTO.class)))).willReturn(tag);
        given(tagMapper.toTagDTO(Mockito.any(Tag.class))).willReturn(tagDTO);
        tagDTO.setName("new Tag");
        System.out.println(tagDTO);
        TagDTO updateTag = tagService.update(1L, tagDTO);
        System.out.println(updateTag);
        Assertions.assertThat(updateTag.getName()).isEqualTo("new Tag");
    }

    @Test
    void getById() {
        given(tagMapper.toTagDTO(tag)).willReturn(tagDTO);
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
        TagDTO byId = tagService.getById(1L);
        System.out.println(byId);
        Assertions.assertThat(byId).isNotNull();
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
        System.out.println(tagListDTO);
        given(tagRepository.findAll(pageable)).willReturn(new PageImpl<>(tagList));
        given(tagMapper.toTagDTOList(Mockito.anyList())).willReturn(tagListDTO);
        List<TagDTO> allTags = tagService.getAllTags(pageable);
        System.out.println(allTags);
        Assertions.assertThat(allTags).isNotNull();
        Assertions.assertThat(allTags.size()).isEqualTo(2);
    }

    @Test
    void delete() {
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));
        tagService.delete(1L);
        verify(tagRepository, times(1)).delete(Mockito.any(Tag.class));
    }

}
