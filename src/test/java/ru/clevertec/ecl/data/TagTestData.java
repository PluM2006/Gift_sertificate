package ru.clevertec.ecl.data;

import java.util.Arrays;
import java.util.List;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;

public class TagTestData {

  public static Tag buildTagOne() {
    return Tag.builder()
        .id(1L)
        .name("New Tag").build();
  }

  public static Tag buildTagTwo() {
    return Tag.builder()
        .id(2L)
        .name("New Tag 2").build();
  }

  public static TagDTO buildTagDTO() {
    return TagDTO.builder()
        .id(1L)
        .name("New Tag").build();
  }

  public static List<Tag> buildTags(){
    return Arrays.asList(buildTagOne(), buildTagTwo());
  }

  public static List<TagDTO> buildTagsDTO() {
    return Arrays.asList(buildTagDTO());
  }
}
