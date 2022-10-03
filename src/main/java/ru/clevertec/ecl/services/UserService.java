package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.entity.User;

import java.util.List;

public interface UserService {

    User getUserByUserName(String username);

    List<User> getAllUsers(Pageable pageable);


}
