package ru.clevertec.ecl.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    private User user;
    private UserDTO userDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = Pageable.ofSize(1);
        user = getUser();
        userDTO = getUserDTO();
    }

    @Test
    void getUserByUserName() {
        given(userRepository.findUserByUsername("userName")).willReturn(Optional.of(user));
        given(userMapper.toUserDTO(user)).willReturn(userDTO);
        UserDTO userDTOByUsername = userService.getUserByUserName("userName");
        assertAll(() -> assertThat(userDTO).isNotNull(),
                () -> assertEquals(userDTOByUsername.getUsername(), userDTO.getUsername()));
    }

    @Test
    void getUserByUserNameNotFoundException() {
        given(userRepository.findUserByUsername("userName")).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserByUserName("userName"));
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(
                User.builder()
                        .firstName("firstName_2")
                        .username("userName_2")
                        .secondName("secondName_2")
                        .id(2L).build());
        users.add(getUser());
        given(userRepository.findAll(pageable)).willReturn(new PageImpl<>(users));
        List<UserDTO> allUsers = userService.getAllUsers(pageable);
        assertAll(()->assertThat(allUsers).isNotNull(),
                ()->assertEquals(allUsers.size(), 2));
    }

    private User getUser() {
        return User.builder()
                .firstName("firsName")
                .secondName("secondName")
                .username("userName")
                .id(1L)
                .build();
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
                .firstName("firstName")
                .secondName("secondName")
                .username("userName")
                .id(1L).build();
    }

}
