package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.services.OrderService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getALlOrdersUser(@Valid @RequestBody UserDTO userDTO,
                                                           @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllUserOrder(userDTO, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
