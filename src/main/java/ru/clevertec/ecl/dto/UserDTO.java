package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String firstName;
    private String secondName;
    private String username;
}
