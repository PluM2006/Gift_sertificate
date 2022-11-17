package ru.clevertec.ecl.data;

import java.util.Arrays;
import java.util.List;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;

public class UserTestData {

  public static UserDTO buildUserDTO() {
    return UserDTO.builder()
        .id(1L)
        .secondName("secondName")
        .firstName("firstName")
        .username("username")
        .build();
  }

  public static User buildUserOne() {
    return User.builder()
        .id(1L)
        .secondName("secondName")
        .firstName("firstName")
        .username("username")
        .build();
  }

  public static User buildUserTwo() {
    return User.builder()
        .id(2L)
        .secondName("secondName 2")
        .firstName("firstName 2")
        .username("username 2")
        .build();
  }

  public static List<User> buildUsers() {
    return Arrays.asList(buildUserOne(), buildUserTwo());
  }
}
