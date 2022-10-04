package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDTO addOrder(OrderDTO orderDTO);

    List<OrderDTO> getAllUserOrder(UserDTO userDTO);

    OrderDTO getOrderByNumberOrder(UUID uuid);
}
