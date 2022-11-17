package ru.clevertec.ecl.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.services.OrderService;

@Slf4j
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO) {
    log.info("REQUEST: method = POST, path = /v1/orders, body = {}", orderDTO);
    OrderDTO order = orderService.addOrder(orderDTO);
    log.info("RESPONSE: responseBody = {}", order);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<List<OrderDTO>> getALlOrdersUser(@Valid @RequestBody UserDTO userDTO,
                                                         @PageableDefault Pageable pageable) {
    log.info("REQUEST: method = GET, path = /v1/orders, body = {}, pageable = {}", userDTO, pageable);
    List<OrderDTO> allUserOrders = orderService.getAllUserOrders(userDTO, pageable);
    log.info("RESPONSE: responseBody = {}", allUserOrders);
    return ResponseEntity.ok(allUserOrders);
  }

  @GetMapping("/offset")
  public ResponseEntity<List<OrderDTO>> getAllOrderUserOffset(@Valid @RequestBody UserDTO userDTO,
                                                              @RequestParam(required = false) int limit,
                                                              @RequestParam(required = false) int offset) {
    log.info("REQUEST: method = GET, path = /v1/orders/offset, body = {}, param = [limit = {}, offset = {}", userDTO,
        limit, offset);
    List<OrderDTO> allUserOrders = orderService.getAllUserOrdersOffset(userDTO, limit, offset);
    log.info("RESPONSE: responseBody = {}", allUserOrders);
    return ResponseEntity.ok(allUserOrders);

  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
    log.info("REQUEST: method = GET, path = /v1/orders/{}", id);
    OrderDTO orderById = orderService.getOrderById(id);
    log.info("RESPONSE: responseBody = {}", orderById);
    return ResponseEntity.ok(orderById);
  }

  @PostMapping(value = "/sequence/set")
  public long setSequence(@RequestBody Long seq) {
    return orderService.setSequence(seq);
  }

  @GetMapping(value = "/sequence/current")
  public long getLastValueSequence() {
    return orderService.getLastValueSequence();
  }

}
