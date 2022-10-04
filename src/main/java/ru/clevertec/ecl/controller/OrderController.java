package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.services.OrderService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrderUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(orderService.getAllUserOrder(userDTO));
    }
}
