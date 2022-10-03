package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.services.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User getUserByUserName(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers(Pageable pageable) {
        return null;
    }
}
