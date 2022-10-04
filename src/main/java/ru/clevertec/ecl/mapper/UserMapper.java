package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}
