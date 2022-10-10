package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getUserByUserName(String username);

    List<UserDTO> getAllUsers(Pageable pageable);
}
