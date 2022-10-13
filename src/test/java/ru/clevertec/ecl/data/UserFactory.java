package ru.clevertec.ecl.data;

import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;

public class UserFactory {

    public static UserDTO userDTO() {
        return UserDTO.builder()
                .id(1L)
                .secondName("secondName")
                .firstName("firstName")
                .username("username")
                .build();
    }

    public static User user() {
        return User.builder()
                .id(1L)
                .secondName("secondName")
                .firstName("firstName")
                .username("username")
                .build();
    }
}
