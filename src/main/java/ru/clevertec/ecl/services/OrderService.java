package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;

import java.util.List;

public interface OrderService {

    OrderDTO addOrder(OrderDTO orderDTO);

    List<OrderDTO> getAllUserOrder(UserDTO userDTO);

    OrderDTO getOrderById(Long id);

}
