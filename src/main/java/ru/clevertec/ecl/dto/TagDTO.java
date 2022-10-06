package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class TagDTO {

    private Long id;

    @NotBlank
    private String name;

}
