package ru.clevertec.ecl.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;

public interface OrderService {

  OrderDTO addOrder(OrderDTO orderDTO);

  List<OrderDTO> getAllUserOrders(UserDTO userDTO, Pageable pageable);

  OrderDTO getOrderById(Long id);

  long getNextValueSequence(Long seq);

  long getLastValueSequence();
}
