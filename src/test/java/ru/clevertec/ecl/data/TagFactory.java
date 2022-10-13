package ru.clevertec.ecl.data;

import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entity.Tag;

public class TagFactory {

    public static Tag tag() {
        return Tag.builder()
                .id(1L)
                .name("New Tag").build();
    }

    public static TagDTO tagDTO() {
        return TagDTO.builder()
                .id(1L)
                .name("New Tag").build();
    }
}
