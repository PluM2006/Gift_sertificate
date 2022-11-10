package ru.clevertec.ecl.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.User;

public interface OrderService {

  OrderDTO addOrder(OrderDTO orderDTO);

  List<OrderDTO> getAllUserOrders(UserDTO userDTO, Pageable pageable);

  OrderDTO getOrderById(Long id);

  long setSequence(Long seq);

  long getLastValueSequence();

  List<OrderDTO> getAllUserOrdersOffset(UserDTO userDTO, int limit, int offset);
}
