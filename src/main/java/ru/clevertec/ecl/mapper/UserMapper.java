package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;

@Mapper
public interface UserMapper {

    UserDTO toUserDTO(User user);

    @Mapping(target = "orderList", ignore = true)
    User toUser(UserDTO userDTO);
}
