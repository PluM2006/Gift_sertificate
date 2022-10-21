package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class TagDTO {

    private Long id;

    @NotBlank
    private String name;
}
