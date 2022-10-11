package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.services.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toUserDTO(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", "id", id)));
    }

    @Override
    public List<UserDTO> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable)
                .map(userMapper::toUserDTO)
                .toList();
    }
}
