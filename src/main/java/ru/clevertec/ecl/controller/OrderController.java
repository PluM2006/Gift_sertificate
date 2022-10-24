package ru.clevertec.ecl.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.services.OrderService;

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
    return ResponseEntity.ok(orderService.getAllUserOrders(userDTO, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @PostMapping(value = "/sequence/next")
  public long getNextSequence(@RequestBody Long seq) {
    return orderService.getNextValueSequence(seq);
  }

  @GetMapping(value = "/sequence/current")
  public long getLastValueSequence() {
    return orderService.getLastValueSequence();
  }
}
