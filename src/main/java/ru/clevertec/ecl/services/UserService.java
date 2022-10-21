package ru.clevertec.ecl.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDTO;

public interface UserService {

  UserDTO getUserById(Long id);

  List<UserDTO> getAllUsers(Pageable pageable);
}
