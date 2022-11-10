package ru.clevertec.ecl.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.services.UserService;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserForId(@PathVariable Long id) {
    log.info("REQUEST: method = GET, path = /v1/users/{}", id);
    UserDTO userById = userService.getUserById(id);
    log.info("RESPONSE: responseBody = {}", userById);
    return ResponseEntity.ok(userById);
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUser(@PageableDefault Pageable pageable) {
    log.info("REQUEST: method = GET, path = /v1/users, param = [pageable = {}]", pageable);
    List<UserDTO> allUsers = userService.getAllUsers(pageable);
    log.info("RESPONSE: responseBody = {}", allUsers);
    return ResponseEntity.ok(allUsers);
  }

}
