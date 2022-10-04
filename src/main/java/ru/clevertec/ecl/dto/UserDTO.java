package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String firstName;
    private String secondName;
    private String username;
    private List<OrderDTO> orderListDto;
}
