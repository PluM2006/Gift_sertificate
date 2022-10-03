package ru.clevertec.ecl.services;

import org.springframework.stereotype.Service;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order addOrder(Order order);

    List<Order> getAllUserOrder(User user);

    Order getOrderUser(User user, UUID uuid);
}
