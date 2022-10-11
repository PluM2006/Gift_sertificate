package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String firstName;
    private String secondName;

    @NotBlank
    private String username;
}
