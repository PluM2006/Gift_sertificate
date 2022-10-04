package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;

import java.util.List;

public interface UserService {

    UserDTO getUserByUserName(String username);

    List<UserDTO> getAllUsers(Pageable pageable);


}
